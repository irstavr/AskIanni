package QueryEvaluation;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;


public class OKAPIBM25 implements RetrievalModel {
	Vocabulary Voc;
	private LinkedList<String> docsIDsList;
	
    public OKAPIBM25() {
		this.Voc = new Vocabulary();
	}
    
    public ScoreEntry[] evaluateQuery(String query) throws IOException {
    	//Get num of docs
    	int numDocs = Vocabulary.getNumOfDocuments();

    	//List with scores for every doc
    	LinkedList<ScoreEntry> scores = new LinkedList<>();
    
    	//Tokenize the query
    	String[] tokens = tokenizeQuery(query);
                
    	double avgdl = Voc.getAvgdl();
        double score;

        //get the list with all the docsIDs
        Iterator<String> it = docsIDsList.iterator();

        while (it.hasNext()) {
            String docID = it.next();
            score = scoreOKAPIBM25(tokens, numDocs, docID, avgdl, 2.0, 0.75);
            if (score > 0) {
                scores.add(new ScoreEntry((new Double(score)).floatValue(), docID));
            }
        }
        ScoreEntry[] results = (ScoreEntry[]) scores.toArray(new ScoreEntry[scores.size()]);        
  
        return results;
    }

    
    private double scoreOKAPIBM25(String[] tokens,int numDocs, String docID, double avgdl, double k, double b) throws IOException {
		double sum = 0;
        for (int i = 0; i < tokens.length; i++) {
            double d = ( numDocs - QueryResults.getTermDF(tokens[i]) + 0.5 ) / QueryResults.getTermDF(tokens[i]) + 0.5;
            double idf = Math.log10(d);
            double tf = QueryResults.getTermTFInDoc(tokens[i], docID);
            sum += idf * (tf * (k + 1.0)) / (tf + k * (1 - b + b * (numDocs / avgdl)));
        }
        return sum;
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

	@Override
	public void setDocsList(LinkedList<String> docsList) {
		this.docsIDsList = docsList;
	}
}
