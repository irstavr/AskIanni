package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mitos.stemmer.Stemmer;

public class VectorSpaceModel implements RetrievalModel {
	private HashMap<String, VocabularyEntry> voc;
	private HashMap<String, HashMap<String, Double>> termWeights;
	private HashMap<String, Double> qTermWeights;
	private Set<String> docsIDs;
	private int numDocs;

	public VectorSpaceModel(HashMap<String, VocabularyEntry> voc) throws IOException {
		this.voc = voc;
		this.termWeights = new HashMap<String, HashMap<String, Double>>();
		this.qTermWeights = new HashMap<String, Double>();
		this.numDocs = Vocabulary.getNumOfDocuments();
		Stemmer.Initialize();
	}

	public HashMap<String, Double> evaluateQuery(String[] token) throws IOException {
		HashMap<String, Double> results = new HashMap<String, Double>();
		
		docTermWeight();
		// creates vector
		for(int i = 0; i<token.length; i++)
		{
			System.out.println("Token : " + token[i]);
			QueryGUI.addToQmap(token[i]);

			float qTf = QueryGUI.getqMapTF(token[i])
					/ (float) QueryGUI.getqMaxFreq();
			double qIdf = 0;
			if(QueryResults.getTermDFs().get(token[i])!=null)
			 qIdf = calcInverdedDF(QueryResults.getTermDF(token[i]));
			double queryWeight = qTf * qIdf;
			this.addqTermMap(token[i], queryWeight);

			HashMap<String, HashMap<String, Float>> termTFs = QueryResults
					.getTermTFs();
			for (String term : termTFs.keySet()) {
				//System.out.print("Term :  " + term + "\t");
				double termIdf = calcInverdedDF(QueryResults.getTermDF(
						term));
				HashMap<String, Float> termDocTfs = termTFs.get(term);
				for (String docID : termDocTfs.keySet()) {
				//	System.out.print("Doc : " + docID);
					double termWeight = termDocTfs.get(docID) * termIdf;
					addTermMap(docID, token[i], termWeight);
				}
			}
		}

		// calculate cosSim
		double innerProduct = 0; // means eswteriko ginomeno
		double wVectLen = 0, qVectLen = queryVectorLength();
		double finalScore = 0;

		for(int i = 0; i<token.length; i++)
		{
			for (String docID : this.termWeights.keySet()) {
				if (this.termWeights.get(docID).get(token[i]) != null) {
					innerProduct += this.termWeights.get(docID).get(token[i])* this.qTermWeights.get(token[i]);
					wVectLen += termVectorLength(docID);
					finalScore = (innerProduct / (Math.sqrt(wVectLen * qVectLen)));
					results.put(docID, finalScore);
				}
			}
		}
		wVectLen = 0;

		return (HashMap<String, Double>) this.sortByValue(results);
	}

	private void docTermWeight() throws IOException {
		for (String s : voc.keySet()) {

			// Find term on vocabulary and its datas
			VocabularyEntry term = this.voc.get(s);

			/* if term is not found -- none results! */
			if (term != null) {

				int posStart = term.getPosStart();
				int bytesLength = term.getBytesLength();
				
				/* Get the infos from PostingFile */
				getFromPostFile(posStart, bytesLength);
			}
		}
	}

	private void getFromPostFile(int posStart, int bytesLength)	throws IOException {
		RandomAccessFile file = new RandomAccessFile("PostingFile.txt", "r");

		file.seek(posStart);

		while (file.getFilePointer() < posStart + bytesLength) {
			String line = file.readLine();

			String[] lineStrings = line.split("\\s+");

			byte[] bytes = lineStrings[1].getBytes("UTF-8");
			String term = new String(bytes, "UTF-8");

			// exception in case the term does not exist on the map
			if ( voc.containsKey(term) ) {
				double termIdf = calcInverdedDF(voc.get(term).getDf());
				double termTf = Double.parseDouble(lineStrings[2]);
				double termWeight = termIdf * termTf;
				this.addTermMap(lineStrings[0], term, termWeight);
			}
		}
		
		file.close();
	}

	private double queryVectorLength() {
		double sum = 0;
		for (Double qtf : this.qTermWeights.values()) {
			sum += Math.pow(qtf, 2);
		}
		return sum;
	}

	private double termVectorLength(String docid) {
		double sum = 0;
		HashMap<String, Double> map = termWeights.get(docid);
		for (String str : map.keySet()) {
			sum += Math.pow(map.get(str), 2);
		}

		return sum;
	}

	@SuppressWarnings("unused")
	private double termVectorLength() {
		double sum = 0;
		for (Double qtf : this.qTermWeights.values()) {
			sum += Math.pow(qtf, 2);
		}
		return sum;
	}

	private void addTermMap(String docID, String word, double TermWeight) {
		HashMap<String, Double> wordsTF = this.termWeights.get(docID);
		if (wordsTF == null) {
			HashMap<String, Double> t = new HashMap<String, Double>();
			t.put(word, TermWeight);
			this.termWeights.put(docID, t);
		} else {
			wordsTF.put(word, TermWeight);
		}
	}

	private void addqTermMap(String word, double qTermWeight) {
		this.qTermWeights.put(word, qTermWeight);
	}

	/* returns back the w = tf * log(N/df) */
	@SuppressWarnings("unused")
	private float calcWeight(float tf, float df, int N) {
		float d = (float) N / (float) df;
		return (float) (tf * Math.log10(d)); // TO CHANGE: log2
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

	   //  Collections.reverse(list);
	     /*   for (int i=0;i<list.size();i++)
	     {
	    	 System.out.println("list i : " + list.get(i));
	     }*/
	     
	     Map<K, V> result = new LinkedHashMap<K, V>();
	     for (Map.Entry<K, V> entry : list)
	     {
	    	 //System.out.println("entry key : " + entry.getKey() + "entry val : " + entry.getValue());
	         result.put( entry.getKey(), entry.getValue() );
	     }
	     
	     //for(K s : result.keySet())
	    	// System.out.println("String s " + s );
	     return result;
	 }
	 
	private double calcInverdedDF(float df) {
		return Math.log(numDocs / df) / Math.log(2);
	}

	@Override
	public void setDocsList(Set<String> docsList) {
		this.docsIDs = docsList;
	}

	/**
	 * @return the docsIDs
	 */
	public Set<String> getDocsIDs() {
		return docsIDs;
	}


}
