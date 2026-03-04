import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.*;
import java.util.ArrayList;
public class Twitt {
    public State state;
    public String text;
    public String filePath = "E:/University/family_tweets2014.txt";

    public Twitt(String text){
        this.text = text;
    }
    public List<Twitt> loadFromFile()
    {
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

    public void textParcer(){

    }
}
