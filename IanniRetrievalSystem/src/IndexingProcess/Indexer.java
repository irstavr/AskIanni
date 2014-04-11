package IndexingProcess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
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
	private static final String DOCUMENT = "DocumentsFile.txt";

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

		// B1. print number of different words of the input files
		// printNumOfDiffWords(index);

		printPostingList(index);
		// B4. build VocabularyFile
		// buildVocabularyFile(index);

		// B5. build DocumentFile
		// buildDocumentFile();
	}

	/* Returns the size of keys of the index (sum of diff words) */
	private static void printNumOfDiffWords(
			HashMap<Word, LinkedList<PostingListNode>> index) {
		System.out.println("#Num_of_diff_words: " + index.keySet().size());
	}



	private static void printPostingList(SortedSet<Document> index) {
		

		StringBuilder postLine = new StringBuilder();
		StringBuilder vocLine = new StringBuilder();
		int nextPos = 0;
		
		//create files
		createFile(POSTING);
		createFile(VOCABULARY);
		
		
		for (String word : vocabulary.keySet()) {
			postLine = new StringBuilder();
			for  (Entry<String, Document> entry : documents.entrySet()) {
				Document doc = entry.getValue();
				if (doc.getPosList().containsKey(word)) {
					//postLine += String.format("%5d \t %-15s \t", doc.getDocumentID(), word);
					postLine.append(doc.getDocumentID()).append("\t").append(word).append("\t");
					for (int i = 0; i < doc.getPosList().get(word).size(); i++) {
						//postLine += String.format("%3d", doc.getPosList().get(word).get(i));
						postLine.append(doc.getPosList().get(word).get(i)).append(" ");
					}
					//postLine += "\n";
					postLine.append("\n");
				}
			}
			//System.out.println("Line.len = " + line.length() + "\n" + line);
			// postline += doc.getLeng
			//System.out.println("Post" + postLine.toString());
			writeToFile(POSTING, postLine.toString() , nextPos);

			//System.out.printf("%-15s  \t %3d \t %3d \t %3d\n", word, vocabulary.get(word),nextPos, postLine.length());
			vocLine.append(word).append("\t").append(vocabulary.get(word)).append("\t").append(nextPos).append(" ").append(postLine.length()).append("\n");
			//next Position for posting File
			nextPos += postLine.length() + 1;			

		}
		System.out.println("vocLine : \n" + vocLine.length());
		writeToFile(VOCABULARY,vocLine.toString(),0);
		
		
		
		
		//test reading from file
		try {
			System.out.println("\n\n\n******REading:");
			//readFromFile(POSTING,0 ,line.length());
			System.out.println(readFromFile(POSTING,0,170));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//at this point we may clear all collections we have created before except Vocabulary
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

	private static void createFile(String outputFile) {

		/* output file location is identified from the configuration file */
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile("CollectionIndex\\" + outputFile, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeToFile(String filePath, String str,
			int position) {
		RandomAccessFile file;
		try {
			file = new RandomAccessFile(filePath, "rw");
			file.seek(position);
			file.write(str.getBytes());
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	private static void closeFile(RandomAccessFile file) {
		try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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