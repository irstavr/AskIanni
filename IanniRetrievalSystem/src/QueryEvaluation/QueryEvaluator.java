package QueryEvaluation;

import java.util.HashMap;


public class QueryEvaluator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        System.out.println("Evaluation Process...");
        
        /* Load Vocabulary from File into memory */
        Vocabulary voc = new Vocabulary();
        voc.setVocabulary("VocabularyFile.txt");
		HashMap<String,VocabularyEntry> vocabulary = voc.getVocabulary();        

		QueryGUI.main(vocabulary);
    }
}