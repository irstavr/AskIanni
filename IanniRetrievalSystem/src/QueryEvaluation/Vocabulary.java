package QueryEvaluation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;


/**
 * Class that represents the vocabulary retrieved by CollectionIndex/VocabularyFile.txt
 *
 */
public class Vocabulary {	

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
			
			for (String line; (line=file.readLine()) != null; ) {
				
				// TO-DO: comment out these lines!
				// just for testing!
				System.out.println(line);

				String[] lineStrings = line.split(" ",4);

				System.out.print("term: "+lineStrings[0]);
				System.out.print(" df: "+lineStrings[1]);
				System.out.print(" pos1: "+lineStrings[2]);
				System.out.println(" pos2: "+lineStrings[3]);

				// add from file to the vocabulary
				addToVocabulary(lineStrings[0],Long.parseLong(lineStrings[1]),Integer.parseInt(lineStrings[2]),Integer.parseInt(lineStrings[3]));				
			}		
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
