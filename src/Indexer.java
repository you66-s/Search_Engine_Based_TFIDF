import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Indexer {
    private static int jsonVersion = 1;
    private Map<String, Map<String, Double>> index; // doc -> (mot -> poids)
    private FileCorpus fileCorpus;
    private List<String> filesPaths;
    private final String indexFilePath;


    public Indexer(String dataset) {
        this.indexFilePath = "index_v" + Indexer.jsonVersion + ".json";
        this.filesPaths = new ArrayList<>();
        File directory = new File(dataset);
        File[] files = directory.listFiles();
        if (files != null){
            for (File file: files){
                this.filesPaths.add(file.getAbsolutePath());
            }
            System.out.println("File Corpus creation start...");
            this.fileCorpus = new FileCorpus(this.filesPaths);
            System.out.println("File Corpus creation done...");
            this.index = new HashMap<>();
        }else {
            System.out.println("Empty directory");
            this.index = new HashMap<>();
            this.fileCorpus = null;
        }
    }
    public void buildIndex(){
        if (this.fileCorpus == null) {
            System.err.println("FileCorpus not initialized because of empty dataset.");
            return;
        }
        System.out.println("Index building Started....");
        for (String path : this.filesPaths) {
            try {
                Documents document = new Documents(path);
                Map<String, Double> tfidfVector = fileCorpus.TFIDF(document);
                index.put(document.getId(), tfidfVector);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'indexation du document " + path + ": " + e.getMessage());
            }
        }
        System.out.println("Index building done ....");
        this.saveIndexToJSON();
    }
    private void saveIndexToJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(this.indexFilePath)){
            gson.toJson(this.index, writer);
            Indexer.jsonVersion++;
            System.out.println("Index sauvegardé avec succès dans " + indexFilePath);
        }catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de l'index : " + e.getMessage());
        }
    }
}
