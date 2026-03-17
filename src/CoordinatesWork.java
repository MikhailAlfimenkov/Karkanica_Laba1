import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class CoordinatesWork {

    private List<Map<String, Object>> tweets;
    private final StateLocator stateLocator;

    public CoordinatesWork(String jsonPath, StateLocator stateLocator) {
        this.stateLocator = stateLocator;
        loadTweets(jsonPath);
    }

    @SuppressWarnings("unchecked")
    private void loadTweets(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            tweets = mapper.readValue(new File(path),
                    new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2,
                     double minX, double minY,
                     double scale, double offsetX, double offsetY,
                     double panelHeight) {

        if (tweets == null) return;

        g2.setColor(Color.RED);

        for (Map<String, Object> t : tweets) {
            double lon = (double) t.get("coordinateY"); // долгота
            double lat = (double) t.get("coordinateX"); // широта

            double px = (lon - minX) * scale + offsetX;
            double py = panelHeight - ((lat - minY) * scale + offsetY);

            g2.fillOval((int) px - 3, (int) py - 3, 6, 6);

            if (stateLocator != null) {
                String state = stateLocator.findState(lon, lat);
                System.out.println("Tweet at (" + lat + ", " + lon + ") in state: " + state);
            }
        }
    }
}