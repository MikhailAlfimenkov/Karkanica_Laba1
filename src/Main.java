import java.util.List;

public class Main {
    public static void main(String[] args) {
        Twitt twitt = new Twitt("");
        List<Twitt> twitts = twitt.loadFromFile();
        for (Twitt t : twitts) {
            t.twittParcer();
        }
        if (twitts.isEmpty()) {
            System.out.println("твитов нету");
        } else {
            for (Twitt t : twitts) {
                System.out.println(t.text);
            }

        }
        Twitt.saveToFile(twitts, "twitts.json");

    }
}