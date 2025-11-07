import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // reading file
        String filePath = "Dataset/original_documents/doc1.txt";
        Documents doc = new Documents(filePath);
        String content = doc.getContent();

        //Apply treatment
        TextTraitement traitement = new TextTraitement(content);
        Map<String, Integer> terms = traitement.countTerms();
        // Calculate the terms frequencies:
        Map<String, Double> frequencies = doc.termsFrequency(terms);
        System.out.println("Terms frequency: \n");
        System.out.println(frequencies);

        //TODO:
    }
}
