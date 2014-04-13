package QueryEvaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;


public class VectorSpaceModel implements RetrievalModel{
	private HashMap<String,VocabularyEntry> voc;
	private Set<String> docsIDs;
	private int numDocs;
	
	public VectorSpaceModel(HashMap<String,VocabularyEntry> voc) throws IOException {
		this.voc = voc;
		this.numDocs = Vocabulary.getNumOfDocuments();
	}

	public HashMap<String,ScoreEntry> evaluateQuery(String query) {
		HashMap<String,ScoreEntry> results = new HashMap<String,ScoreEntry>();
		float[] q = vectorizeQuery(query);
        float qNorm = calcNorm(q);

       // int numDocs = Vocabulary.getNumOfDocuments();
        ArrayList<Float> scores = new ArrayList<Float>(numDocs);
        float score;

        Iterator<String> it = docsIDs.iterator();
        while (it.hasNext()) {
            String ID = it.next();
//            float tf[] = termFrequencies.get(ID);
//
//           // QueryResults.getTermTFs().get(query).get(ID); // tf for word==query in doc with docID = ID
//            
//            for (int i = 0; i < tf.length; i++) {
//                tf[i] = calcWeight(tf[i], (float) termsDocsFreqs[i], numDocs);
//            }
//
//            score = score(tf, q, calcNorm(tf), calcNorm(q));
//            if (score > 0) {
//                scores.add(new ScoreEntry(score, ID));
//            }
        }
        
       // ScoreEntry[] results = (ScoreEntry[]) scores.toArray(new ScoreEntry[scores.size()]);
       // Arrays.sort(results, new ScoreComparator());
        
        return results;
	}

	private float score(float[] tf, float[] q, float dNorm, float qNorm) {
		int size = tf.length;
        float dXq = 0.0f;
        for (int i = 0; i < size; i++) {
            dXq += tf[i] * q[i];
        }
        return (float) dXq / (dNorm * qNorm);
	}

	/* returns back the w = tf * log(N/df) */
	private float calcWeight(float tf, float df, int N) {
        float d = (float) N / (float) df;
        return (float) (tf * Math.log10(d));			// TO CHANGE:  log2
    }
	
    private float calcNorm(float[] v) {
        float sum = 0.0f;
        for (int i = 0; i < v.length; i++) {
            sum += Math.pow(v[i], 2);
        }
        return (float) Math.sqrt(sum);
    }


    private float[] vectorizeQuery(String query) {
        float[] q = new float[voc.size()];
        for (int i = 0; i < q.length; i++) {
            q[i] = 0.0f;
        }
        String[] tokens = tokenizeQuery(query);
        int numTokens = tokens.length;
//        for (int i = 0; i < numTokens; i++) {
//            if (voc.containsKey(tokens[i])) {
//                int pos = invIndex.getTermPosition(tokens[i]);
//                
//                float tf = 1.0f / (float) numTokens;
//                int df = (int) invIndex.getTermDocFreq(tokens[i]);
//                q[pos] = calcWeight(tf, df, numDocs);
//            }
//        }
        return q;
    }

    private String[] tokenizeQuery(String query) {
        StringTokenizer stok = new StringTokenizer(query.toLowerCase());
        int numTokens = stok.countTokens();
        String[] tokens = new String[numTokens];
        int i = 0;
        while (stok.hasMoreTokens()) {
            String token = stok.nextToken();
            tokens[i++] = token;
        }
        return tokens;
    }

	@Override
	public void setDocsList(Set<String> docsList) {
		this.docsIDs = docsList;
		
	}

}
