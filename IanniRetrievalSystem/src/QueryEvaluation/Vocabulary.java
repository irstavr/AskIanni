package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;


/**
 * Class that represents the vocabulary retrieved by CollectionIndex/VocabularyFile.txt
 *
 */
public class Vocabulary {	
	private static int numOfDocs;
	private HashMap<String,VocabularyEntry> vocabulary = new HashMap<>();

	/* Gets the vocabulary */
	public HashMap<String,VocabularyEntry> getVocabulary() {
		return vocabulary;
	}

	/* Add an entry to the vocabulary structure */
	public void addToVocabulary(String text, long df, int start, int end) {
		VocabularyEntry entry = new VocabularyEntry(df, start, end);		
		vocabulary.put(text, entry);		
	}
	
	/* Loads VocabularyFile.txt and saves it on our structure 'vocabulary' */
	public void setVocabulary(String vocabularyFileName) {
		
		System.out.println("\n\n\n******Loading Vocabulary File:");

		// Reading from file
		try {
			RandomAccessFile file = new RandomAccessFile(vocabularyFileName, "r");
			
			String firstLine = file.readLine();
			numOfDocs = Integer.parseInt(firstLine);
			System.out.println("N="+numOfDocs);
			//line=new String(file.readLine().getBytes("UTF-8"),"UTF-8")
			for (String line; (line=file.readLine()) != null; ) {
				
				// TO-DO: comment out these lines!
				// just for testing!
				String[] lineStrings = line.split("\\s+");
				
			
				String term = new String(lineStrings[0].getBytes("UTF-8"), "UTF-8");
				/*System.out.print("term: "+term);
				System.out.print(" | df: "+lineStrings[1]);
				System.out.print(" | startPos: "+lineStrings[2]);
				System.out.println(" | readBytes: "+lineStrings[3]);
*/
				// add from file to the vocabulary
				addToVocabulary(term,Long.parseLong(lineStrings[1]),Integer.parseInt(lineStrings[2]),Integer.parseInt(lineStrings[3]));				
			}		
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/* returns the num of documents that's on the 1st line of VocabularyFIle.txt */
	public static int getNumOfDocuments() throws IOException {
		return numOfDocs;	
	}

	public double getAvgdl() {
		int sum = QueryResults.getSumDocLength();
		return sum/numOfDocs;
	}

	
	
}
