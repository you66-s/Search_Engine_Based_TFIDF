import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.util.*;

public class SearchEngine {
    private Map<String, Map<String, Double>> index;
    private final int topDocument;

    public SearchEngine(int topDocument, String jsonPath){
        this.topDocument = topDocument;
        loadIndex(jsonPath);
    }
    private void loadIndex(String jsonFilePath) {
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Gson gson = new Gson();
            this.index = gson.fromJson(reader, new TypeToken<Map<String, Map<String, Double>>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du JSON : " + e.getMessage());
            this.index = new HashMap<>();
        }
    }
    private Map<String, Double> vectorizeQuery(String query) {
        Documents request = new Documents(query, true);
        TextTraitement traitement = new TextTraitement(query);
        Map<String, Integer> terms = traitement.countTerms();
        Map<String, Double> TF = request.termsFrequency(terms);

        // TFIDF construction
        Map<String, Double> TFIDF = new HashMap<>();
        for (String term: TF.keySet()){
            double idfMot = 0.0;
            for (Map<String, Double> docVector: this.index.values()){
                if (docVector.containsKey(term)){
                    idfMot = docVector.get(term);
                    break;
                }
            }
            TFIDF.put(term, TF.get(term) * idfMot);
        }
        return TFIDF;
    }
    private double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2){
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (String key : vec1.keySet()) {
            double v1 = vec1.get(key);
            double v2 = vec2.getOrDefault(key, 0.0); // vector from json file
            dot += v1 * v2;
            norm1 += v1 * v1;
        }
        for (double val : vec2.values()) {
            norm2 += val * val;
        }
        if (norm1 == 0 || norm2 == 0) return 0.0;
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    public List<SearchResult> search(String query) {
        Map<String, Double> queryVector = vectorizeQuery(query);
        PriorityQueue<SearchResult> pq = new PriorityQueue<>((a, b) -> Double.compare(b.score, a.score));

        for (Map.Entry<String, Map<String, Double>> entry : index.entrySet()) {
            String docName = entry.getKey();
            double score = cosineSimilarity(queryVector, entry.getValue());
            if (score > 0.1) pq.add(new SearchResult(docName, score));
        }

        List<SearchResult> results = new ArrayList<>();
        for (int i = 0; i < this.topDocument && !pq.isEmpty(); i++) {
            results.add(pq.poll());
        }

        return results;
    }
}
