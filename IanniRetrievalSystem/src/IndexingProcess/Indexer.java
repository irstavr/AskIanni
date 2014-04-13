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

	private static final String VOCABULARY = "CollectionIndex\\VocabularyFile.txt";
	private static final String POSTING = "CollectionIndex\\PostingFile.txt";

	private static HashMap<String,Document> documents;
	private static TreeMap<String, Integer> vocabulary;
	// temporary
	static OutputStream fw;
	static PrintStream ps;
	static BufferedWriter bw = null;

	public static void main(String[] args) throws IOException {

		createFolder();
		// B6. builds PostingFile
		// It follows the parsing, stemming(B3) and analyzing
		// of multiple documents (B2)
		InvertedIndex invertedIndex = buildInvertedIndex();

		SortedSet<Document> index = invertedIndex.getIndex();


		createAllFiles();

	}

	/* Returns the size of keys of the index (sum of diff words) */
	private static void printNumOfDiffWords(
			HashMap<Word, LinkedList<PostingListNode>> index) {
		System.out.println("#Num_of_diff_words: " + index.keySet().size());
	}



	private static void createAllFiles() throws IOException {
		

		int nextPos = 0;
		int prevPos = 0;
		RandomAccessFile postingFile = new RandomAccessFile(POSTING, "rw");
		RandomAccessFile vocabularyFile = new RandomAccessFile(VOCABULARY, "rw");
		
		//create files
		//createFile(POSTING);
		//createFile(VOCABULARY);
		
		
		for (String word : vocabulary.keySet()) {
			for  (Entry<String, Document> entry : documents.entrySet()) {
				Document doc = entry.getValue();
				HashMap<String,ArrayList<Integer>> docPosList = doc.getPosList(); 
				if (docPosList.containsKey(word)) {
					postingFile.write(Long.toString(doc.getDocumentID()).getBytes(Charset.forName("UTF-8")));
					postingFile.write((" " + word + " ").getBytes(Charset.forName("UTF-8")));
					ArrayList<Integer> wordList = docPosList.get(word);
					for (int i = 0; i < wordList.size(); i++) {
						postingFile.write((" " + Integer.toString(wordList.get(i))).getBytes(Charset.forName("UTF-8")));
					}
					postingFile.write((" " + Integer.toString(doc.getDocsLinePos()) + " ").getBytes(Charset.forName("UTF-8")));
					postingFile.write(Integer.toString(doc.getDocsLineSize()).getBytes(Charset.forName("UTF-8")));
					postingFile.write(System.getProperty("line.separator").getBytes(Charset.forName("UTF-8")));
				}	
			}
			prevPos = nextPos;
			nextPos = (int) postingFile.length();	
			
			System.out.println("Write Length : " + prevPos + "  " + nextPos);
			vocabularyFile.write((word + " ").getBytes(Charset.forName("UTF-8")));
			vocabularyFile.write((vocabulary.get(word) + " ").getBytes(Charset.forName("UTF-8")));
			vocabularyFile.write((" " + Integer.toString(prevPos)).getBytes(Charset.forName("UTF-8")));
			vocabularyFile.write((" " + Integer.toString(nextPos-prevPos)).getBytes(Charset.forName("UTF-8")));
			vocabularyFile.write(System.getProperty("line.separator").getBytes(Charset.forName("UTF-8")));
		}
		//test reading from file
		try {
			System.out.println("\n\n\n******REading:");
			//readFromFile(POSTING,0 ,line.length());
			System.out.println(readFromFile(POSTING,141,39));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		postingFile.close();
		vocabularyFile.close();
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




	private static String readFromFile(String filePath, int position, int size)
			throws IOException {

		RandomAccessFile file = new RandomAccessFile(filePath, "r");
		file.seek(position);
		byte[] bytes = new byte[size];
		file.read(bytes);
		System.out.println("File Length : " + file.length());
		file.close();
		return new String( bytes);
	}
	
	
	/* Builds the inverted index, reads all documents */
	public static InvertedIndex buildInvertedIndex() throws IOException {
		Document d;

		/* Input Files */
		File dir = new File("documentCollection");
		File[] inputFiles = dir.listFiles();

		InvertedIndex index = new InvertedIndex();
		index.clear();

		/*
		 * now lets parse the input file we call the parser that returns a set
		 * of Document objects
		 */
		Parser parser = new Parser(inputFiles);
		parser.readDocuments();
		documents = parser.getDocsList();
		vocabulary = parser.getVocabulary();
		
		/* put the document list into the InvertedIndex */
		index.setDocumentList(documents);

	/*	for (Entry<String, Document> entry : documents.entrySet()) {
		    System.out.print("key,val: ");
		    System.out.println(entry.getKey() + "," + entry.getValue().getDocumentID());
		    index.add(entry.getValue());
		}*/

		return index;
	}
}