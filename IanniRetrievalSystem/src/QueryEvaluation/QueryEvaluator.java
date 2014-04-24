package QueryEvaluation;

import java.io.IOException;
import java.util.HashMap;


public class QueryEvaluator {

	public static void main(String[] args) throws IOException {
        System.out.println("Evaluation Process...");
        
        /* Load Vocabulary from File into memory */
        Vocabulary voc = new Vocabulary();
        voc.setVocabulary("VocabularyFile.txt");
		HashMap<String,VocabularyEntry> vocabulary = voc.getVocabulary();        

		/* Create GUI */
		QueryGUI.main(vocabulary);
    }
}