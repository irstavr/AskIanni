package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;


public class QueryResults {
	private ArrayList<String> results;
	HashMap<String,VocabularyEntry> voc;
	ScoreEntry[] scoreEntry;
	RetrievalModel model;
	private ArrayList<String> paths;
	String text;

	public QueryResults(HashMap<String,VocabularyEntry> voc, RetrievalModel model, String text) throws IOException {
		this.voc = voc;
		this.model = model;
		this.text = text;
		this.results = new ArrayList<String>();
        this.scoreEntry = model.evaluateQuery(text);
        this.paths = new ArrayList<String>();
	}

	public ArrayList<String> getResults() {
		return results;
	}

	public ArrayList<String> createResults() throws IOException {
		// Find term on vocabulary and its datas
		VocabularyEntry term = this.voc.get(text);

		/* if term is not found -- none results! */
		if ( term != null ) {
			/* Get the df of this term */
			long df = term.getDf();

			/* Get the pointer to the PostingFile.txt */
			int posStart = term.getPosStart();
			int bytesLength   = term.getBytesLength();

			System.out.println("\nResults for Query:"+text);
			System.out.println("df: "+df+ " | PosStart: " + posStart + " | PosEnd: " + bytesLength);

			/* Get the infos from PostingFile */
			getDocsFromPostingFile(posStart,bytesLength);
			
			evaluateQuery(text);
			
		} else {
			results.add("None result found!");
		}

		return this.results;
	}

	private void evaluateQuery(String text) {
        this.scoreEntry = model.evaluateQuery(text);
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
			System.out.print("| tfTerm: "+lineStrings[1]);
			System.out.print("| posStart: "+lineStrings[2]);
			System.out.print("| bytesToRead: "+lineStrings[3]);

			System.out.println("| positions:");
			for (int i=4; i< lineStrings.length; i++) {
				System.out.println(" "+ lineStrings[i]);
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
            System.out.println("Line= "+line);
            
			String[] lineStrings = line.split("\\s+");

			for (int i=0; i<lineStrings.length; i++) {
				System.out.println("_"+i+"_"+lineStrings[i]);
			}
			
			/* Add string of document path to print it on GUI results */
			getPaths().add(lineStrings[1]);
		}		
		file.close();		
	}

	public ArrayList<String> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}

}