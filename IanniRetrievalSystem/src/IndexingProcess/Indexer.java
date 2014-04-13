package IndexingProcess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;

/* This class builds the inverted index.
 * 
 * Phases:
 * Acquire content -> Build Document -> Analyze Doc -> Index Doc -> Indexer
 * 
 * After parsing, documents are fed to an 'add' method that adds an inverted index.
 * The inverted index consists of both a term dictionary and , for each term,
 * a posting list that is a list of documents that contain the term.
 * 
 * We make the assumption that the entire inverted index fits in primary memory.
 */
public class Indexer {

	private static final String VOCABULARY = "VocabularyFile.txt";
	private static final String POSTING = "PostingFile.txt";

	private static File[] inputFiles;
	private static HashMap<Long, Document> documents;
	private static TreeMap<String, Word> vocabulary;
	private static HashMap<Long, Short> maxtfdoc;
	// temporary
	static OutputStream fw;
	static PrintStream ps;
	static BufferedWriter bw = null;

	public static void main(String[] args) throws IOException {

		createFolder();
		// B6. builds PostingFile
		// It follows the parsing, stemming(B3) and analyzing
		// of multiple documents (B2)
		long start, stop;
		start = System.currentTimeMillis();
	
		Parser parser = new Parser();
		parser.readDocuments();
		documents = parser.getDocsList();
		vocabulary = parser.getVocabulary();
		maxtfdoc = parser.getMaxtfdoc();
		
		createAllFiles();
		stop = System.currentTimeMillis();
		printNumOfDiffWords();
		long fulltime = stop - start;
		fulltime = (fulltime / 1000)% 60;
		System.out.println(fulltime);

	}

	/* Returns the size of keys of the index (sum of diff words) */
	private static void printNumOfDiffWords(
			) {
		System.out.println("#Num_of_diff_words: " + vocabulary.size());
	}


	private static void createAllFiles() throws IOException {

		int nextPos = 0;
		int prevPos = 0;
		RandomAccessFile postingFile = new RandomAccessFile(POSTING, "rw");
		RandomAccessFile vocabularyFile = new RandomAccessFile(VOCABULARY, "rw");

		
		vocabularyFile.write((Integer.toString(documents.size())).getBytes(Charset.forName("UTF-8")));
		vocabularyFile.write(System.getProperty("line.separator").getBytes(
				Charset.forName("UTF-8")));

		for (Entry<String, Word> vocEntry : vocabulary.entrySet()) {
			String wordStr = vocEntry.getKey();
			Word word = vocEntry.getValue();
		//	System.out.printf("Word : %-15s \tDocument Frequency : %3d\n", wordStr , word.getDocFreq());
			for (Entry<Long, ArrayList<Integer>> entry : word.getDocPosMap()
					.entrySet()) {
				Long docId = entry.getKey();
				ArrayList<Integer> docPosList = entry.getValue();
				// System.out.print ("Doc ID  " + entry.getKey() + " ");

				postingFile.write(Long.toString(docId).getBytes(
						Charset.forName("UTF-8")));
				postingFile.write((" " + wordStr + " ").getBytes(Charset
						.forName("UTF-8")));
				Float termFreq = (float) (word.getWordFreqMap().get(docId)
						.getTF() / (float) maxtfdoc.get(docId));
				postingFile.write(Float.toString(termFreq).getBytes(
						Charset.forName("UTF-8")));
				postingFile.write((" " + Integer.toString(documents.get(docId)
						.getDocsLinePos())).getBytes(Charset.forName("UTF-8")));
				postingFile
						.write((" " + Integer.toString(documents.get(docId)
								.getDocsLineSize())).getBytes(Charset
								.forName("UTF-8")));
				for (int i = 0; i < docPosList.size(); i++)
					postingFile
							.write((" " + Integer.toString(docPosList.get(i)))
									.getBytes(Charset.forName("UTF-8")));
				postingFile.write(System.getProperty("line.separator")
						.getBytes(Charset.forName("UTF-8")));
			}

			prevPos = nextPos;
			nextPos = (int) postingFile.length();
	
			vocabularyFile.write((wordStr + " ").getBytes(Charset.forName("UTF-8")));

			vocabularyFile.write(Integer.toString(word.getDocFreq()).getBytes(
					Charset.forName("UTF-8")));
			vocabularyFile.write((" " + Integer.toString(prevPos))
					.getBytes(Charset.forName("UTF-8")));
			vocabularyFile.write((" " + Integer.toString(nextPos - prevPos))
					.getBytes(Charset.forName("UTF-8")));
			//vocabularyFile.write("\n".getBytes("UTF-8"));
			vocabularyFile.write(System.getProperty("line.separator").getBytes(
					Charset.forName("UTF-8")));
		}
		
		
		//vocabularyFile.write("γιαννης".getBytes(Charset.forName("UTF-8")));
	/*	vocabulary.clear();
		documents.clear();
		maxtfdoc.clear();*/
		postingFile.close();
		vocabularyFile.close();
		
		readFromFile("my.txt", 0, 100);
	}

	private static void createFolder() {
		File file = new File("CollectionIndex");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	private static void readFromFile(String filePath, int position, int size)	throws IOException {


		RandomAccessFile file = new RandomAccessFile(VOCABULARY, "rw");
		file.seek(position);
		byte[] bytes = new byte[size];
		file.read(bytes);

		String s = new String(bytes, "UTF-8");
		System.out.println(s);

		file.close();

	
	}


}