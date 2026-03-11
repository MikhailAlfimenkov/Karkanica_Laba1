import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public static List<double[]> loadCoordinatesFromJson(String jsonFilePath) {
    ObjectMapper mapper = new ObjectMapper();
    List<double[]> coordinates = new ArrayList<>();
    try {
        List<Twitt> twitts = mapper.readValue(new File(jsonFilePath), new TypeReference<List<Twitt>>() {});
        for (Twitt twitt : twitts) {
            coordinates.add(new double[]{twitt.coordinateX, twitt.coordinateY});
        }
    } catch (IOException e) {
        System.out.println("Ошибка чтения JSON: " + e.getMessage());
    }
    return coordinates;
}