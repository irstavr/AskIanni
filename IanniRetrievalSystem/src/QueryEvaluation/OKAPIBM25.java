package QueryEvaluation;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import mitos.stemmer.Stemmer;


public class OKAPIBM25 implements RetrievalModel {
	Vocabulary Voc;
	private Set<String> docsIDsList;

    public OKAPIBM25() {
		this.Voc = new Vocabulary();
		Stemmer.Initialize();
	}

    public HashMap<String,Double> evaluateQuery(String[] token) throws IOException {
    	//List with scores for every doc
    	HashMap<String,Double> docScores = new HashMap<String, Double>();
    	
    	//Get num of docs
    	int numDocs = Vocabulary.getNumOfDocuments();
                
    	double avgdl = Voc.getAvgdl();
        double score;

        //get the list with all the docsIDs
        Iterator<String> it = docsIDsList.iterator();

        while (it.hasNext()) {
            String docID = it.next();
            score = scoreOKAPIBM25(token, numDocs, docID, avgdl, 2.0, 0.75);
            if (score > 0) {
            	docScores.put(docID, new Double(score) );
            }
        }

        return (HashMap<String, Double>) this.sortByValue(docScores);
    }


    public  <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
	     List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
		 Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
		         public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
		         {
		             return (o2.getValue()).compareTo( o1.getValue() );
		         }
		     } 
		 );
	     
	     Map<K, V> result = new LinkedHashMap<K, V>();
	     for (Map.Entry<K, V> entry : list) {
	         result.put( entry.getKey(), entry.getValue() );
	     }
	     return result;
	 }

	private double scoreOKAPIBM25(String[] tokens,int numDocs, String docID, double avgdl, double k, double b) throws IOException {
		double sum = 0;
        for (int i = 0; i < tokens.length; i++) {
            double d = ( numDocs - QueryResults.getTermDF(tokens[i]) + 0.5 ) / QueryResults.getTermDF(tokens[i]) + 0.5;
            double idf = Math.log(d) / Math.log(2);
            
            idf = Math.abs(idf);
            
            double tf = QueryResults.getTermTFInDoc(tokens[i], docID);
            sum += idf * (tf * (k + 1.0)) / (tf + k * (1 - b + b * (numDocs / avgdl)));
        }
        return sum;
    }

    /*
     * Tokenize the query into tokens
     * TOADD: stop words, stemming!
     */
    @SuppressWarnings("unused")
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
	public void setDocsList(Set<String> docsList) {
		this.docsIDsList = docsList;
	}
}
