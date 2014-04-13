package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class QueryResults {
	String query;
	private ArrayList<String> results;
	HashMap<String,VocabularyEntry> voc;
	ScoreEntry[] scoreEntry;
	RetrievalModel model;
	private QueryGUI gui;
	private HashMap<String,String> docsIDPathMap;						/* key: docID, value: path */
	
	private HashMap<String,ArrayList<Long>> wordPos;					/* key: docID, value: positions of this word in it 	*/
	private HashMap<String, HashMap<String, ArrayList<Long>>> pos;	/* key: word, value: map<DocID,list<pos>> */
	
	private static HashMap<String,Float> termDFs;						/* key: term string, value: its df 					*/
	private static HashMap<String,HashMap<String,Float>> termTFs;		/* key: term , value: map<docID,tf>					*/

	public QueryResults(QueryGUI gui, HashMap<String,VocabularyEntry> voc, RetrievalModel model, String query) throws IOException {
		this.gui = gui;
		this.voc = voc;
		this.model = model;
		this.query = query;
		this.results = new ArrayList<String>();
        this.scoreEntry = null;
        this.docsIDPathMap = new HashMap<String,String>();
        this.wordPos = new HashMap<String,ArrayList<Long>>();
        this.pos = new HashMap<String, HashMap<String, ArrayList<Long>>>();
        QueryResults.setTermDFs(new HashMap<String,Float>());
        QueryResults.setTermTFs(new HashMap<String, HashMap<String,Float> >());
	}

	public ArrayList<String> getResults() {
		return results;
	}

	public ArrayList<String> createResults() throws IOException {
		
		String[] terms = query.split("\\s+");
		
		// tokenized query terms
		for ( int i=0; i<terms.length; i++ ) {			
			
			// Find term on vocabulary and its datas
			VocabularyEntry term = this.voc.get(terms[i]);
	
			/* if term is not found -- none results! */
			if ( term != null ) {
				/* Get the df of this term */
				float df = term.getDf();
	
				/* add df of this term to the map */
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
		HashMap<String,ScoreEntry> scores = model.evaluateQuery(query);
		
		/* print infos to the GUI */
		Iterator<Entry<String,String>> it = docsIDPathMap.entrySet().iterator();
		
		while (it.hasNext()) {
			Entry<String,String> entry =  it.next();			

			gui.getTextArea().append("Doc:"+ entry.getKey());
			gui.getTextArea().append(" | Path"+ entry.getValue());
			gui.getTextArea().append(" | Score"+ scores.get(entry.getKey()));
			gui.getTextArea().append(" | " + getSnippet(entry.getValue(),entry.getKey())+"\n");
		}
		return this.results;
	}

	/* Retrieves from this docID located at this path, a snippet that contains the query word */
	private String getSnippet(String path, String docID) throws IOException {
		RandomAccessFile file = new RandomAccessFile(path, "r");
		String line=null;
		
		String[] terms = query.split("\\s+");
		
		for ( int i =0; i<terms.length; i++ ) {
			
			String word = terms[i];
			
			if ( pos.containsKey(word)) {
				HashMap<String, ArrayList<Long>> docPos = pos.get(word);
				
				if ( docPos.containsKey(docID) ) {
					/* get the positions of the query word ( ONE WORD ) on this doc */
					ArrayList<Long> positions = docPos.get(docID);
					
					/* get just one example of the positions */
					file.seek(positions.get(1));
					while(file.getFilePointer() < positions.get(i)+50) {
			            line = file.readLine();
			            System.out.println("Snippet ("+docID+" "+ pos.get(i)+") = "+line);
					}
				}
			}
		}
		file.close();

		return line;
	}

	/* Read from file PostingFile.txt from position posStart for bytesLength bytes */
	private void getDocsFromPostingFile(int posStart, int bytesLength) throws IOException {
		RandomAccessFile file = new RandomAccessFile("PostingFile.txt", "r");

		file.seek(posStart);
				
		while(file.getFilePointer() < posStart+bytesLength) {
            String line = file.readLine();
            System.out.println(line);
            
			String[] lineStrings = line.split("\\s+");
			System.out.print("docID: "+lineStrings[0]);
			System.out.print(" | term: "+lineStrings[1]);
			System.out.print(" | tfTerm: "+lineStrings[2]);
			System.out.print(" | posStart: "+lineStrings[3]);
			System.out.print(" | bytesToRead: "+lineStrings[4]);

			System.out.print(" | positions:");
			for (int i=5; i< lineStrings.length; i++) {
				System.out.print(" "+ lineStrings[i]);
				
				if ( wordPos.containsKey(lineStrings[0]) ) {
					ArrayList<Long> pos = wordPos.get(lineStrings[0]);	//get pos list of this DocID
					pos.add(Long.parseLong(lineStrings[i]));				//add new pos to the list
					wordPos.put(lineStrings[0], pos);						//update map with new list
				} else {
					ArrayList<Long> pos = new ArrayList<Long>();	//create new list with only this pos
					pos.add(Long.parseLong(lineStrings[i]));
					wordPos.put(lineStrings[0], pos);
				}
				/* Update the map that stores: term - positions on this doc */
				pos.put(lineStrings[1], wordPos);
			}
			
			/* store TF into the map */
			if ( getTermTFs().containsKey(lineStrings[1]) ) {				
				HashMap<String,Float> tfs = getTermTFs().get(lineStrings[1]);
				tfs.put(lineStrings[0], Float.parseFloat(lineStrings[2]));
				getTermTFs().put(lineStrings[1], tfs);			
			} else {
				HashMap<String,Float> tfs = new HashMap<>();
				tfs.put(lineStrings[0], Float.parseFloat(lineStrings[2]));
				getTermTFs().put(lineStrings[1], tfs);
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

		while(file.getFilePointer() < posStart+bytesLength) {
            String line = file.readLine();
            System.out.println("\n\nPostingFileLine= "+line);
 
			String[] lineStrings = line.split("\\s+");
			
			docsIDPathMap.put(lineStrings[0], lineStrings[1]);	/* add the docID with its path to the map */
			
			//String format = lineStrings[2];
			//long docLength = lineStrings[3];
		}
		file.close();
	}

	
	public static float getTermDF(String term) {
		return getTermDFs().get(term);
	}

	public static float getTermTFInDoc(String term, String docID) {
		
		HashMap<String,Float> tfs = getTermTFs().get(term);
		return tfs.get(docID);
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
}