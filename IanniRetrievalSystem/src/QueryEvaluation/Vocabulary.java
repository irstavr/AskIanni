package QueryEvaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


/**
 * Class that represents the vocabulary retrieved by CollectionIndex/VocabularyFile.txt
 *
 */
public class Vocabulary {
	
	private HashMap<String,VocabularyEntry> vocabulary = new HashMap<>();

	
	public HashMap<String,VocabularyEntry> getVocabulary() {
		return vocabulary;
	}

	public void addToVocabulary(String text, long df, int start, int end) {
		VocabularyEntry entry = new VocabularyEntry(df, start, end);		
		vocabulary.put(text, entry);		
	}
	
	public void setVocabulary(String vocabularyFileName) {
				
		BufferedReader br = null;
		 
		try { 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(vocabularyFileName));
 
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
	}
}
