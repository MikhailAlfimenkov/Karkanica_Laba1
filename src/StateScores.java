import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateScores {

    private Map<String, Double> avgScores = new HashMap<>();

    public StateScores(List<TwittC> tweets, StateLocator locator) {

        Map<String, Double> sum = new HashMap<>();
        Map<String, Integer> count = new HashMap<>();

        for (TwittC t : tweets) {
            String state = locator.findState(t.coordinateY, t.coordinateX);
            if (state == null) continue;

            sum.put(state, sum.getOrDefault(state, 0.0) + t.score);
            count.put(state, count.getOrDefault(state, 0) + 1);
        }

        for (String state : sum.keySet()) {
            avgScores.put(state, sum.get(state) / count.get(state));
        }
    }

    public double getScore(String state) {
        return avgScores.getOrDefault(state, 0.0);
    }

    public boolean hasTweets(String state) {
        return avgScores.containsKey(state);
    }

}
