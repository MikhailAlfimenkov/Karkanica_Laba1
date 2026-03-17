import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

public class TwittScore {

    public static List<TwittC> loadAndScoreTweets(String sentimentsPath, String tweetsPath) {

        Map<String, Double> wordSentiments = new HashMap<>();
        int maxWord = 0;

        // Загружаем словарь
        try (Scanner scanner = new Scanner(new File(sentimentsPath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String phrase = parts[0].toLowerCase().trim();
                    Double score = Double.valueOf(parts[1].trim());
                    wordSentiments.put(phrase, score);

                    int wc = phrase.split("\\s+").length;
                    if (wc > maxWord) maxWord = wc;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Загружаем твиты
        ObjectMapper mapper = new ObjectMapper();
        List<TwittC> tweets = new ArrayList<>();

        try {
            tweets = mapper.readValue(new File(tweetsPath),
                    new com.fasterxml.jackson.core.type.TypeReference<List<TwittC>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Считаем score
        for (TwittC t : tweets) {
            double totalScore = 0;

            String cleanText = t.text.toLowerCase()
                    .replaceAll("@[\\w]+", "")
                    .replaceAll("https?://\\S+", "")
                    .replaceAll("[^a-zа-яё0-9\\s]", " ")
                    .trim();

            String[] words = cleanText.split("\\s+");

            for (int i = 0; i < words.length; i++) {
                for (int window = maxWord; window >= 1; window--) {
                    if (i + window <= words.length) {
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < window; j++) {
                            sb.append(words[i + j]);
                            if (j < window - 1) sb.append(" ");
                        }
                        String phrase = sb.toString();

                        if (wordSentiments.containsKey(phrase)) {
                            totalScore += wordSentiments.get(phrase);
                            i += (window - 1);
                            break;
                        }
                    }
                }
            }

            t.score = totalScore;
        }

        return tweets;
    }
}
