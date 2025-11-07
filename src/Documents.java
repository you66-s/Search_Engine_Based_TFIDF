import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Documents {
    private String content;
    private int totalTerms;

    public Documents(String filePath){
        File document = new File(filePath);
        StringBuilder sb = new StringBuilder();
        try(Scanner reader = new Scanner(document)){
            while(reader.hasNextLine()){
                sb.append(reader.nextLine()).append(" ");
            }
            this.content = sb.toString().trim();
            TextTraitement traitement = new TextTraitement(this.content);
            this.totalTerms = traitement.countTerms().values().stream().mapToInt(Integer::intValue).sum();
        } catch (FileNotFoundException e) {
            System.out.println("Error While reading the file: "+ e);
        }
    }
    public Map<String, Double> termsFrequency(Map<String, Integer> terms){
        Map<String, Double> termFrequency = new HashMap<>();
        for(String term: terms.keySet()){
            int count = terms.get(term);
            double freq = (double) count / this.totalTerms;
            freq = Math.round(freq * 10000.0) / 10000.0;
            termFrequency.put(term, freq);
        }
        return termFrequency;
    }
    public String getContent() {
        return this.content;
    }
}
