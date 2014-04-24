package QueryEvaluation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Class that represents the vocabulary retrieved by CollectionIndex/VocabularyFile.txt
 *
 */
public class Vocabulary {	
	private static int numOfDocs;
	private HashMap<String,VocabularyEntry> vocabulary = new HashMap<>();
	private static HashMap<String, Integer> stopWords = new HashMap<>();
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

	 public void setVocabulary(String vocabularyFileName) throws IOException {
		System.out.println("******Loading Vocabulary File");

		stopWords = readStopWordFiles();
		// Reading from file
		try {
			BufferedReader file = new BufferedReader(new InputStreamReader(
					new FileInputStream(vocabularyFileName), "UTF8"));

			String firstLine = file.readLine();
			numOfDocs = Integer.parseInt(firstLine);
			// System.out.println("N="+numOfDocs);

			for (String line; (line = file.readLine()) != null;) {
				String[] lineStrings = line.split("\\s+");
				//String term = new String(lineStrings[0].getBytes("UTF-8"),
					//	"UTF-8");
				String term = lineStrings[0];
			//	System.out.println("term: " + term);
		
				// add from file to the vocabulary
				addToVocabulary(term, Long.parseLong(lineStrings[1]),
						Integer.parseInt(lineStrings[2]),
						Integer.parseInt(lineStrings[3]));
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		private static HashMap<String, Integer> readStopWordFiles()
				throws IOException {
			FileReader fileReaderGr = new FileReader("stopwordsGr.txt");
			FileReader fileReaderEn = new FileReader("stopwordsEn.txt");

			BufferedReader bufferedReaderGr = new BufferedReader(fileReaderGr);
			BufferedReader bufferedReaderEn = new BufferedReader(fileReaderEn);

			HashMap<String, Integer> stopWords = new HashMap<String, Integer>();
			String stopWordGr, stopWordEn = null;

			// parse stopWordsGr
			while ((stopWordGr = bufferedReaderGr.readLine()) != null) {
				stopWords.put(stopWordGr, stopWordGr.length()); // add stop word to
																// list
			}
			bufferedReaderGr.close();

			// parse stopWordsEn
			while ((stopWordEn = bufferedReaderEn.readLine()) != null) {
				stopWords.put(stopWordEn, stopWordEn.length()); // add stop word to
																// list
			}
			bufferedReaderEn.close();

			return stopWords;
		}
	/* returns the num of documents that's on the 1st line of VocabularyFIle.txt */
	public static int getNumOfDocuments() throws IOException {
		return numOfDocs;	
	}

	public double getAvgdl() {
		int sum = QueryResults.getSumDocLength();
		return sum/numOfDocs;
	}

	public static HashMap<String, Integer> getStopWords() {
		return stopWords;
	}

	public void setStopWords(HashMap<String, Integer> stopWords) {
		this.stopWords = stopWords;
	}	
}
