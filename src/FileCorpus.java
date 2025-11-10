import java.util.*;

public class FileCorpus {
    private final List<Documents> documents;
    private final Map<String, Double> IDF;
    private final Set<String> vocabulary;

    public FileCorpus(List<String> filePaths) {
        this.documents = new ArrayList<>();
        this.vocabulary = new HashSet<>();
        this.IDF = new HashMap<>();

        for (String path : filePaths) {
            Documents doc = new Documents(path);
            this.documents.add(doc);
            TextTraitement traitement = new TextTraitement(doc.getContent());
            this.vocabulary.addAll(traitement.countTerms().keySet()); //Construction de vocabulaire
        }
        calculateIDF();
    }
    // IDF For all documents
    private void calculateIDF(){
        int D = this.documents.size();
        Map<String, Integer> docFreq = new HashMap<>();
        for (Documents document: this.documents){
            Set<String> termsInDoc = new TextTraitement(document.getContent()).countTerms().keySet();
            for (String term: termsInDoc){
                docFreq.put(term, docFreq.getOrDefault(term, 0) + 1);
            }
        }
        //IDF
        for (String term: this.vocabulary){
            int DF = docFreq.getOrDefault(term, 0);
            double IDF = Math.log((double) D / DF );
            this.IDF.put(term, IDF);
        }
    }
    //TF-IDF of a document
    public Map<String, Double> TFIDF(Documents document){
        String content = document.getContent();
        TextTraitement traitement = new TextTraitement(content);
        Map<String, Integer> termCounts = traitement.countTerms();
        Map<String, Double> tfMap = document.termsFrequency(termCounts);
        Map<String, Double> tfidfMap = new HashMap<>();
        //TF-IDF for each term
        for (String term: tfMap.keySet()){
            double tf = tfMap.get(term);
            double idf = this.IDF.getOrDefault(term, 0.0);
            double tfidf = tf * idf;
            tfidfMap.put(term, Math.round(tfidf * 10000.0) / 10000.0);
        }
        return tfidfMap;
    }

    public Set<String> getVocabulary() {
        return this.vocabulary;
    }
}
