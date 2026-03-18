import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StateLocator {

    private Map<String, Object> states;

    public StateLocator(Map<String, Object> states) {
        this.states = states;
    }

    @SuppressWarnings("unchecked")
    private void extractRings(Object obj, List<List<List<Double>>> out) {
        if (!(obj instanceof List)) return;

        List<?> list = (List<?>) obj;
        if (list.isEmpty()) return;

        Object first = list.get(0);

        if (first instanceof Number && list.size() == 2) return;

        if (first instanceof List && ((List<?>) first).size() == 2 &&
                ((List<?>) first).get(0) instanceof Number) {
            out.add((List<List<Double>>) list);
            return;
        }

        for (Object child : list) {
            extractRings(child, out);
        }
    }

    private boolean pointInPolygon(double x, double y, List<List<Double>> ring) {
        boolean inside = false;

        for (int i = 0, j = ring.size() - 1; i < ring.size(); j = i++) {
            double xi = ring.get(i).get(0);
            double yi = ring.get(i).get(1);
            double xj = ring.get(j).get(0);
            double yj = ring.get(j).get(1);

            boolean intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi + 1e-12) + xi);

            if (intersect)
                inside = !inside;
        }

        return inside;
    }

    public String findState(double lon, double lat) {
        for (Map.Entry<String, Object> entry : states.entrySet()) {
            String stateName = entry.getKey();
            Object stateObj = entry.getValue();

            List<List<List<Double>>> rings = new ArrayList<>();
            extractRings(stateObj, rings);

            for (List<List<Double>> ring : rings) {
                if (pointInPolygon(lon, lat, ring)) {
                    return stateName;
                }
            }
        }
        return null;
    }
}
