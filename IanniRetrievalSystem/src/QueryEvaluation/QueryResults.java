package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import mitos.stemmer.Stemmer;

public class QueryResults {
	private static String query;
	private static ArrayList<String> results;
	private static HashMap<String,VocabularyEntry> voc;
	private static RetrievalModel model;
	private static HashMap<String,String> docsIDPathMap;					/* key: docID, value: path */
	
	private static HashMap<String,ArrayList<Long>> wordPos;					/* key: docID, value: positions of this word in it 	*/
	private static HashMap<String, HashMap<String, ArrayList<Long>>> pos;	/* key: word, value: map<DocID,list<pos>> */
	
	private static HashMap<String,Float> termDFs;						/* key: term string, value: its df 					*/
	private static HashMap<String,HashMap<String,Float>> termTFs;		/* key: term , value: map<docID,tf>					*/
	private static int sumDocLength;

	public QueryResults(HashMap<String,VocabularyEntry> voc, RetrievalModel model, String query) throws IOException {
		QueryResults.voc = voc;
		QueryResults.model = model;
		QueryResults.query = query;
		QueryResults.results = new ArrayList<String>();
		QueryResults.docsIDPathMap = new HashMap<String,String>();
		QueryResults.wordPos = new HashMap<String,ArrayList<Long>>();
		QueryResults.pos = new HashMap<String, HashMap<String, ArrayList<Long>>>();
        QueryResults.setTermDFs(new HashMap<String,Float>());
        QueryResults.setTermTFs(new HashMap<String, HashMap<String,Float> >());
        QueryResults.sumDocLength = 0;
	}

	public ArrayList<String> getResults() {
		return results;
	}

    private String[] tokenizeQuery(String query) {
    	Stemmer.Initialize();
        StringTokenizer tokenized = new StringTokenizer(query.toLowerCase());
        int numTokens = tokenized.countTokens();
        String[] tokens = new String[numTokens];
        int i = 0;
        while (tokenized.hasMoreTokens()) {
            String token = tokenized.nextToken();
            token = Stemmer.Stem(token);
            tokens[i] = token;
            i++;
        }
        return tokens;
    }
    
	public ArrayList<String> createResults() throws IOException {
		
		String[] terms = tokenizeQuery(query);
		
		// tokenized query terms
		for ( int i=0; i<terms.length; i++ ) {
						
			// Find term on vocabulary and its datas
			VocabularyEntry term = QueryResults.voc.get(terms[i]);

			/* if term is not found -- none results! */
			if ( term != null ) {
				/* Get the df of this term */
				float df = term.getDf();
	
				/* Add df of this term to the map */
				getTermDFs().put(terms[i], df);
				
				/* Get the pointer to the PostingFile.txt */
				int posStart = term.getPosStart();
				int bytesLength = term.getBytesLength();
			
				/* Get the infos from PostingFile */
				getDocsFromPostingFile(posStart,bytesLength);
			} else {
				results.add("None result found!");
			}
		}
		
		/* get the score depending on the selected model */
		model.setDocsList(docsIDPathMap.keySet());	
		Map<String, Double> scores = model.evaluateQuery(terms);
		
		/* print infos to the GUI */
		Iterator<Entry<String,Double>> it = scores.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String,Double> entry =  it.next();

			results.add(entry.getKey()+ " " + 							/* docId */
						docsIDPathMap.get(entry.getKey()) + " " + 		/* path */
						entry.getValue() + " " + 						/* score */
						getSnippet( docsIDPathMap.get(entry.getKey()), entry.getKey() ) );	/* snippet */
		}
		
		return QueryResults.results;
	}

	/* Retrieves from this docID located at this path, a snippet that contains the query word */
	private String getSnippet(String path, String docID) throws IOException {
		RandomAccessFile file = new RandomAccessFile(path, "r");
		String line = new String();

		System.out.println("file: "+path);
		
		String[] terms = tokenizeQuery(query);
		
		for ( int i =0; i<terms.length; i++ ) {			
			String word = terms[i];
			
			if ( pos.containsKey(word)) {
				HashMap<String, ArrayList<Long>> docPos = pos.get(word);
				
				if ( docPos.containsKey(docID) ) {
					/* Get the positions of the query word ( ONE WORD ) on this doc */
					ArrayList<Long> positions = docPos.get(docID);
					
					//Sorting positions
					Collections.sort(positions);
					
					for (Long pos : positions) {
						
						/* get just one example of the positions */						
						file.seek(pos);
						System.out.println("position: "+pos);

				        if ( (line = file.readLine()) != null ) {
					    	List<Integer> matches = new BoyerMooreAlg().match(word, line);

					    	for (Integer integer : matches) {
								System.out.println("Match at: " + integer);
					    	}
					    	file.close();
							return line;
				        }
					}
				}
			}
		}
		file.close();

		return "";
	}

	/* Read from file PostingFile.txt from position posStart for bytesLength bytes */
	private void getDocsFromPostingFile(int posStart, int bytesLength) throws IOException {
		RandomAccessFile file = new RandomAccessFile("PostingFile.txt", "r");

		file.seek(posStart);
				
		while(file.getFilePointer() < posStart+bytesLength) {
            String line = file.readLine();            
			String[] lineStrings = line.split("\\s+");
						
			byte[] bytes = lineStrings[1].getBytes("UTF-8");
			String term = new String(bytes,"UTF-8");
			
			for (int i=5; i< lineStrings.length; i++) {
				if ( wordPos.containsKey(lineStrings[0]) ) {
					ArrayList<Long> pos = wordPos.get(lineStrings[0]);	//get pos list of this DocID
					pos.add(Long.parseLong(lineStrings[i]));			//add new pos to the list
					wordPos.put(lineStrings[0], pos);					//update map with new list
				} else {
					ArrayList<Long> pos = new ArrayList<Long>();	//create new list with only this pos
					pos.add(Long.parseLong(lineStrings[i]));
					wordPos.put(lineStrings[0], pos);
				}
				/* Update the map that stores: term - positions on this doc */
				pos.put(lineStrings[1], wordPos);
			}
			
			/* store TF into the map */		
			if ( getTermTFs().containsKey(term) ) {				
				HashMap<String,Float> tfs = getTermTFs().get(term);
				tfs.put(lineStrings[0], Float.parseFloat(lineStrings[2]));
				getTermTFs().put(term, tfs);			
			} else {
		
				HashMap<String,Float> tfs = new HashMap<>();
				tfs.put(lineStrings[0], Float.parseFloat(lineStrings[2]));
				getTermTFs().put(term, tfs);
			}			
			
			/* Get the infos from DocumentsFile */
			getDocFromDocumentsFile(Integer.parseInt(lineStrings[3]), Integer.parseInt(lineStrings[4]));
		}		
		file.close();		
	}

	/* Read from file DocumentsFile.txt from position posStart for bytesLength bytes */
	private void getDocFromDocumentsFile(int posStart, int bytesLength) throws IOException {
		RandomAccessFile file = new RandomAccessFile("DocumentsFile.txt", "r");
		file.seek(posStart);
		//System.out.println("PosStart : " + posStart + "byteslen : " + bytesLength); 
		while(file.getFilePointer() < posStart+bytesLength) {
            String line = file.readLine();
            String[] lineStrings = line.split("\\s+");
            
			docsIDPathMap.put(lineStrings[0], lineStrings[1]);	/* add the docID with its path to the map */
			setSumDocLength(getSumDocLength() + Integer.parseInt(lineStrings[3]));
		}
		file.close();
	}

	
	public static float getTermDF(String term) {
		return getTermDFs().get(term);
	}

	
	public static float getTermTFInDoc(String term, String docID) {
		HashMap<String,Float> tfs = termTFs.get(term);
		if ( tfs.containsKey(docID)) {
			return tfs.get(docID);
		} else {
			return 0;
		}
	}

	/**
	 * @return the termDFs
	 */
	public static HashMap<String,Float> getTermDFs() {
		return termDFs;
	}

	/**
	 * @param termDFs the termDFs to set
	 */
	public static void setTermDFs(HashMap<String,Float> termDFs) {
		QueryResults.termDFs = termDFs;
	}

	/**
	 * @return the termTFs
	 */
	public static HashMap<String,HashMap<String,Float>> getTermTFs() {
		return termTFs;
	}

	/**
	 * @param termTFs the termTFs to set
	 */
	public static void setTermTFs(HashMap<String,HashMap<String,Float>> termTFs) {
		QueryResults.termTFs = termTFs;
	}

	public static int getSumDocLength() {
		return sumDocLength;
	}

	public void setSumDocLength(int sumDocLength) {
		QueryResults.sumDocLength = sumDocLength;
	}
}