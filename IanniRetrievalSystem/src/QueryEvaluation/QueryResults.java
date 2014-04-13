package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class QueryResults {
	String query;
	private ArrayList<String> results;
	HashMap<String,VocabularyEntry> voc;
	ScoreEntry[] scoreEntry;
	RetrievalModel model;
	private QueryGUI gui;
	private LinkedList<String> docsIDsList;
	private ArrayList<String> docsPathsList;
	private HashMap<String,ArrayList<Integer>> wordPos;				/* key: docID, value: positions of this word in it 	*/
	private static HashMap<String,Long> termDFs;					/* key: term string, value: its df 					*/
	private static HashMap<String,HashMap<String,Long>> termTFs;	/* key: term , value: map<docID,tf>					*/

	public QueryResults(QueryGUI gui, HashMap<String,VocabularyEntry> voc, RetrievalModel model, String query) throws IOException {
		this.gui = gui;
		this.voc = voc;
		this.model = model;
		this.query = query;
		this.results = new ArrayList<String>();
        this.scoreEntry = null;
        this.docsIDsList = new LinkedList<String>();
        this.docsPathsList = new ArrayList<String>();
        this.wordPos = new HashMap<String,ArrayList<Integer>>();
        QueryResults.setTermDFs(new HashMap<String,Long>());
        QueryResults.setTermTFs(new HashMap<String, HashMap<String,Long> >());
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
				long df = term.getDf();
	
				/* add df of this term to the map */
				getTermDFs().put(terms[i], df);
				
				/* Get the pointer to the PostingFile.txt */
				int posStart = term.getPosStart();
				int bytesLength = term.getBytesLength();
	
				/* Get the infos from PostingFile */
				getDocsFromPostingFile(terms[i], posStart,bytesLength);
				
				/* get the score depending on the selected model */
				model.setDocsList(docsIDsList);	
							
			} else {
				results.add("None result found!");
			}
		}
		scoreEntry = model.evaluateQuery(query);
		
		/* print infos to the GUI */
		for (int i=0; i<docsIDsList.size(); i++) {
			gui.getTextArea().append("Doc:"+ docsIDsList.get(i));
			gui.getTextArea().append(" | Path"+docsPathsList.get(i));
			gui.getTextArea().append(" | Score"+scoreEntry[i].getScore());
			gui.getTextArea().append(" | " + getSnippet(docsPathsList.get(i),docsIDsList.get(i))+"\n");
		}
		
		return this.results;		
	}

	/* Retrieves from this docID located at this path, a snippet that contains the query word */
	private String getSnippet(String path, String docID) throws IOException {
		RandomAccessFile file = new RandomAccessFile(path, "r");
		String line=null;
		
		ArrayList<Integer> pos = wordPos.get(docID);
		
		for ( int i=0; i<pos.size(); i++) {
			
			file.seek(pos.get(i));
			while(file.getFilePointer() < pos.get(i)+30) {
	            line = file.readLine();
	            System.out.println("Snippet= "+line);	 
			}			
		}		
		file.close();
		
		return line;
	}

	/* Read from file PostingFile.txt from position posStart for bytesLength bytes */
	private void getDocsFromPostingFile(String term, int posStart, int bytesLength) throws IOException {
		RandomAccessFile file = new RandomAccessFile("PostingFile.txt", "r");

		file.seek(posStart);
				
		while(file.getFilePointer() < posStart+bytesLength) {
            String line = file.readLine();
            System.out.println(line);
            
			String[] lineStrings = line.split("\\s+");
			System.out.print("docID: "+lineStrings[0]);
			System.out.print("| tfTerm: "+lineStrings[1]);
			System.out.print("| posStart: "+lineStrings[2]);
			System.out.print("| bytesToRead: "+lineStrings[3]);

			System.out.print("| positions:");
			for (int i=4; i< lineStrings.length; i++) {
				System.out.print(" "+ lineStrings[i]);
				
				if ( wordPos.containsKey(lineStrings[0]) ) {
					ArrayList<Integer> pos = wordPos.get(lineStrings[0]);	//get pos list of this DocID
					pos.add(Integer.parseInt(lineStrings[i]));				//add new pos to the list
					wordPos.put(lineStrings[0], pos);						//update map with new list
				} else {
					ArrayList<Integer> pos = new ArrayList<Integer>(Integer.parseInt(lineStrings[i]));	//create new list with only this pos
					wordPos.put(lineStrings[0], pos);
				}
			}
			
			/* store TF into the map */
			if ( getTermTFs().containsKey(term) ) {				
				HashMap<String,Long> tfs = getTermTFs().get(term);
				tfs.put(lineStrings[0], Long.parseLong(lineStrings[1]));
				getTermTFs().put(term, tfs);			
			} else {
				HashMap<String,Long> tfs = new HashMap<>();
				tfs.put(lineStrings[0], Long.parseLong(lineStrings[1]));
				getTermTFs().put(term, tfs);
			}			
			
			/* Get the infos from DocumentsFile */
			getDocFromDocumentsFile(Integer.parseInt(lineStrings[2]), Integer.parseInt(lineStrings[3]));
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

			docsIDsList.add(lineStrings[0]);	//add the docID to the list
			docsPathsList.add(lineStrings[1]);	//add the doc path to the list
		}
		file.close();
	}

	
	public static long getTermDF(String term) {
		return getTermDFs().get(term);
	}

	public static long getTermTFInDoc(String term, String docID) {
		
		HashMap<String,Long> tfs = getTermTFs().get(term);
		return tfs.get(docID);
	}

	/**
	 * @return the termDFs
	 */
	public static HashMap<String,Long> getTermDFs() {
		return termDFs;
	}

	/**
	 * @param termDFs the termDFs to set
	 */
	public static void setTermDFs(HashMap<String,Long> termDFs) {
		QueryResults.termDFs = termDFs;
	}

	/**
	 * @return the termTFs
	 */
	public static HashMap<String,HashMap<String,Long>> getTermTFs() {
		return termTFs;
	}

	/**
	 * @param termTFs the termTFs to set
	 */
	public static void setTermTFs(HashMap<String,HashMap<String,Long>> termTFs) {
		QueryResults.termTFs = termTFs;
	}
}