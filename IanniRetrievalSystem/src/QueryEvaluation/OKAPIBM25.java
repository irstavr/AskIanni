package QueryEvaluation;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;


public class OKAPIBM25 implements RetrievalModel {

	private HashMap<String,VocabularyEntry> voc;
	
    public OKAPIBM25(HashMap<String,VocabularyEntry> voc) {
		this.voc = voc;
	}
    
    public ScoreEntry[] evaluateQuery(String query) /*throws IOException */{
//    	//get num of docs for this term
//        int numDocs = invIndex.getNumOfDocuments();
//        
//        //list with scores for every doc
//        List scores = new ArrayList(numDocs);
//        
//        //tokenize the query
//        String[] tokens = tokenizeQuery(query);
//                
//        double avgdl = invIndex.getAvgdl();
//        double score;
//
//        //get the list with all the docsIDs
//        List<String> docsIDs = invIndex.getDocsIDs();
//        Iterator<String> it = docsIDs.iterator();
//
//        while (it.hasNext()) {
//            String docID = it.next();
//            score = scoreOKAPIBM25(tokens, numDocs, docID, avgdl, 2.0, 0.75);
//            if (score > 0) {
//                scores.add(new ScoreEntry((new Double(score)).floatValue(), docID));
//            }
//        }
//
//        ScoreEntry[] results = (ScoreEntry[]) scores.toArray(new ScoreEntry[scores.size()]);
//        Arrays.sort(results, new ScoreComparator());
//        
//        return results;
		return null;
    }

    
    private double scoreOKAPIBM25(String[] tokens,int numDocs, String docID, double avgdl, double k, double b) throws IOException {
		return b;
//        double sum = 0;
//        for (int i = 0; i < tokens.length; i++) {
//            double d = ( numDocs - invIndex.getTermDocFreq( tokens[i] ) + 0.5 ) / invIndex.getTermDocFreq(tokens[i]) + 0.5;
//            double idf = Math.log10(d);
//            double tf = invIndex.getTermFreqInDoc(tokens[i], docID);
//            sum += idf * (tf * (k + 1.0)) / (tf + k * (1 - b + b * (numDocs / avgdl)));
//        }
//        return sum;
    }

    /*
     * Tokenize the query into tokens
     * TOADD: stop words, stemming!
     */
    private String[] tokenizeQuery(String query) {
        StringTokenizer tokenized = new StringTokenizer(query.toLowerCase());
        int numTokens = tokenized.countTokens();
        String[] tokens = new String[numTokens];
        int i = 0;
        while (tokenized.hasMoreTokens()) {
            String token = tokenized.nextToken();
            tokens[i++] = token;
        }
        return tokens;
    }	
}
