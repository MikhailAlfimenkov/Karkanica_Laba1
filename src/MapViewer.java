import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapViewer extends JPanel {

    private Map<String, Object> states;

    private double minX = Double.MAX_VALUE;
    private double maxX = -Double.MAX_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxY = -Double.MAX_VALUE;
    private CoordinatesWork tweetRenderer = new CoordinatesWork("twitts.json");

//sda

    public MapViewer() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            states = mapper.readValue(new File("states.json"), new TypeReference<Map<String, Object>>() {});

            computeBounds();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractRings(Object obj, List<List<List<Double>>> out) {
        if (!(obj instanceof List)) return;

        List<?> list = (List<?>) obj;

        if (list.isEmpty()) return;

        Object first = list.get(0);

        // [x, y]
        if (first instanceof Number && list.size() == 2) {
            return;
        }

        // [[x,y], [x,y], ...]
        if (first instanceof List && ((List<?>) first).size() == 2 &&
                ((List<?>) first).get(0) instanceof Number) {
            out.add((List<List<Double>>) list);
            return;
        }

        for (Object child : list) {
            extractRings(child, out);
        }
    }

    private void computeBounds() {
        for (Object stateObj : states.values()) {
            List<List<List<Double>>> rings = new ArrayList<>();
            extractRings(stateObj, rings);

            for (List<List<Double>> ring : rings) {
                for (List<Double> coord : ring) {
                    double x = coord.get(0);
                    double y = coord.get(1);

                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                }
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double panelWidth = getWidth();
        double panelHeight = getHeight();
        double scaleX = panelWidth / (maxX - minX) * 3;
        double scaleY = panelHeight / (maxY - minY) * 3;
        double scale = Math.min(scaleX, scaleY);
        double offsetX = (panelWidth - (maxX - minX) * scale) / 330;
        double offsetY = (panelHeight - (maxY - minY) * scale) / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        for (Object stateObj : states.values()) {

            List<List<List<Double>>> rings = new ArrayList<>();
            extractRings(stateObj, rings);

            Path2D path = new Path2D.Double();

            for (List<List<Double>> ring : rings) {
                boolean first = true;

                for (List<Double> coord : ring) {
                    double x = coord.get(0);
                    double y = coord.get(1);

                    double px = (x - minX) * scale + offsetX;
                    double py = panelHeight - ((y - minY) * scale + offsetY);

                    if (first) {
                        path.moveTo(px, py);
                        first = false;
                    } else {
                        path.lineTo(px, py);
                    }
                }
            }

            g2.setColor(new Color(180, 200, 255));
            g2.fill(path);

            g2.setColor(Color.BLACK);
            g2.draw(path);

        }
        tweetRenderer.draw(g2, minX, minY, scale, offsetX, offsetY, panelHeight);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("US States Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.add(new MapViewer());
        frame.setVisible(true);

    }
}
