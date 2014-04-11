package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;


public class QueryResults {
	private ArrayList<String> results;
	HashMap<String,VocabularyEntry> voc;
	ScoreEntry[] se;
	String text;

	public QueryResults(HashMap<String,VocabularyEntry> voc, ScoreEntry[] se, String text) throws IOException {
		this.voc = voc;
		this.se = se;
		this.text = text;
		this.results = setResults(new ArrayList<String>());
	}

	public ArrayList<String> getResults() {		
		return results;
	}

	public ArrayList<String> setResults(ArrayList<String> results) throws IOException {
		
		// Find term on vocabulary and its datas
		VocabularyEntry term = this.voc.get(text);
		
		/* if term is not found -- none results! */
		if ( term != null ) {
			/* Get the pointer to the PostingFile.txt */
			int posStart = term.getPosStart();
			int bytesLength   = term.getBytesLength();
			
			System.out.println("\nResults for Query:"+text);
			System.out.println("PosStart: " + posStart + " | PosEnd: " + bytesLength);
			
			getDocsFromPostingFile(posStart,bytesLength);
			
			
		} else {
			results.add("None result found!");
		}
		
		return this.results = results;
	}

	@SuppressWarnings("null")
	private void getDocsFromPostingFile(int posStart, int bytesLength) throws IOException {

		RandomAccessFile file = new RandomAccessFile("PostingFile.txt", "r");
		
		//file.seek(posStart);
		
		byte[] b = null ;
		file.read(b, posStart, bytesLength);	// TO-CHANGE! 
		
		System.out.println("Found PostingFIle:"+b.toString());
		file.close();
		
	}


}
