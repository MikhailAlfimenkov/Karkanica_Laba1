import java.awt.*;
import java.util.List;

public class CoordinatesWork {

    private final List<TwittC> tweets;
    private final StateLocator stateLocator;

    public CoordinatesWork(List<TwittC> tweets, StateLocator locator) {
        this.tweets = tweets;
        this.stateLocator = locator;
    }

    private Color getColor(double score) {
        double s = Math.max(0, Math.min(1, score / 1.0));

        int r = (int)(255 * s);
        int g = (int)(200 * (1 - s));
        int b = (int)(220 * (1 - s));

        return new Color(r, g, b);
    }

    public void draw(Graphics2D g2,
                     double minX, double minY,
                     double scale, double offsetX, double offsetY,
                     double panelHeight) {

        if (tweets == null) return;

        for (TwittC t : tweets) {
            double lon = t.coordinateY;
            double lat = t.coordinateX;

            double px = (lon - minX) * scale + offsetX;
            double py = panelHeight - ((lat - minY) * scale + offsetY);

            g2.setColor(getColor(t.score));
            g2.fillOval((int) px - 4, (int) py - 4, 8, 8);
        }
    }
}
