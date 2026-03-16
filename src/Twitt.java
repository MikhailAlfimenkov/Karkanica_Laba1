import java.io.BufferedReader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.*;
import java.util.ArrayList;
public class Twitt {
    public double coordinateX;
    public double coordinateY;
    public String text;
    public Twitt(String text){
        this.text = text;
    }
    public List<Twitt> loadFromFile()
    {
        String filePath = "D:/ForDeveloping/football_tweets2014.txt";
        File file = new File(filePath);
        List<Twitt> twitts = new ArrayList<>();
        if(!file.exists()){
            System.out.println("файл не найден");
            return twitts;
        }
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                if(!line.trim().isEmpty()){
                    twitts.add(new Twitt(line));

                }
            }
        }catch(IOException e){
            System.out.println("ошибка: " + e.getMessage());
        }

        return twitts;
    }

    public void twittParcer(){
        try{
            String[] parts = text.split("\\t");
            String coordPart = parts[0].replace("[","").replace("]","");
            String[] coordinates = coordPart.split(",");
            coordinateX = Double.parseDouble(coordinates[0].trim());
            coordinateY = Double.parseDouble(coordinates[1].trim());
            //String[] dateTime = parts[2].split(" ");
            //date = Integer.parseInt(dateTime[0].replace("_",""));
            //time = Integer.parseInt(dateTime[1].replace(":",""));
            text = parts[3];
        }catch(Exception e){
            System.out.println("ошибка: " + e.getMessage());
        }
        }

        public static void saveToFile(List<Twitt> twitts, String path){
        ObjectMapper mapper = new ObjectMapper();
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("twitts.json"), twitts);
        }catch(IOException e){
            System.out.println("ошибка json");
        }
        }

}
