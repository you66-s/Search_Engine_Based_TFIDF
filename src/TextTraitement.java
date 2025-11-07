import safar.basic.morphology.stemmer.factory.StemmerFactory;
import safar.basic.morphology.stemmer.interfaces.IStemmer;
import safar.basic.morphology.stemmer.model.WordStemmerAnalysis;
import safar.util.tokenization.impl.SAFARTokenizer;
import safar.util.tokenization.interfaces.ITokenizer;
import java.util.regex.Matcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TextTraitement {
    private String text;
    private final HashSet<String> STOP_WORDS;

    public TextTraitement(String text){
        this.text = text;
        this.text = this.textCleaning(this.text);
        this.STOP_WORDS = new HashSet<>();
        try {
            Scanner reader = new Scanner(new File("src/Arabic_stop_words.txt"));
            while (reader.hasNextLine()) {
                STOP_WORDS.add(reader.nextLine().trim().toLowerCase());
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            System.out.println("Erreur: " + exception);
        }
    }
    private String textCleaning(String text) {
        if (text == null) return "";
        text = text.replaceAll("[\\d]", " ");
        text = text.replaceAll("[^\\p{IsArabic}\\s]", " ");
        text = text.replaceAll("\\s+", " ").trim();
        text = text.replaceAll("[إأآا]", "ا");
        text = text.replaceAll("ى", "ي");
        text = text.replaceAll("ؤ", "و");
        text = text.replaceAll("ئ", "ي");
        text = text.replaceAll("ة", "ه");
        text = text.replaceAll("[\\u0610-\\u061A\\u064B-\\u065F\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED]", "");
        text = text.replaceAll("(.)\\1{2,}", "$1");
        return text;
    }
    private List<String> tokenizeText(){
        this.text = this.text.replaceAll("\\d+", " ");
        ITokenizer tokenizer = new SAFARTokenizer();
        String[] tokens = tokenizer.tokenize(this.text.toLowerCase());
        List<String> tokenList = new ArrayList<>(Arrays.asList(tokens));
        tokenList.removeIf(this.STOP_WORDS::contains);
        return tokenList;
    }
    private List<String> stemming() {
        IStemmer stemmer = StemmerFactory.getISRIImplementation();
        List<String> morphemes = new ArrayList<>();
        List<String> tokens = this.tokenizeText();
        for (String token : tokens) {
            List<WordStemmerAnalysis> results = stemmer.stem(token);
            if (results != null && !results.isEmpty()) {
                for(WordStemmerAnalysis stem: results){
                    String resultStr = stem.toString();
                    String morpheme = extractMorpheme(resultStr);
                    morphemes.add(morpheme != null ? morpheme : token);
                }
            } else {
                morphemes.add(token);
            }
        }
        return morphemes;
    }
    public Map<String, Integer> countTerms(){
        Map<String, Integer> termsCount = new HashMap<>();
        List<String> morphemes = this.stemming();
        for (String morpheme : morphemes) {
            termsCount.put(morpheme, termsCount.getOrDefault(morpheme, 0) + 1);
        }
        return termsCount;
    }
    private String extractMorpheme(String resultStr) {
        Matcher matcher = java.util.regex.Pattern.compile("morpheme\\s*=\\s*([^}]+)").matcher(resultStr);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
    public List<String> getTokens(){
        return this.tokenizeText();
    }
}
