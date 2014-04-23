package IndexingProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import mitos.stemmer.Stemmer;

/*
 * Parser returns a list of documents, so when a Parser returns,
 * its results are sent to the index builder.
 */
public class Parser {
	private static final String DOCUMENT = "DocumentsFile.txt";
	private HashMap<Long, Document> docsMap;
	private TreeMap<String, Word> vocabulary;
	private File[] inputFiles;
	private HashMap<String, Integer> stopWords;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private HashMap<Long, Short> maxtfdoc;
	RandomAccessFile docFile;

	/* accepts as a parameter the file to parse and the stop words to use */
	public Parser() throws IOException {
		this.docsMap = new HashMap<Long, Document>();
		this.inputFiles = inputFiles;
		this.stopWords = readStopWordFiles();
		this.setVocabulary(new TreeMap<String, Word>());
		this.maxtfdoc = new HashMap<Long, Short>();
		docFile = new RandomAccessFile(DOCUMENT, "rw");
	}

	/* read and parse, analyze, stem the documents */
	public void readDocuments() throws IOException {
		// parsing file
		docFile.seek(0);

		Stemmer.Initialize();
		parse("documentCollection");

		docFile.close();
		// stop words analyzer
		stopWordsAnalyzer();
		// run stemmer
		runStemmer();

	}

	private void runStemmer() {
		Stemmer.Initialize();
		// px
		Stemmer.Stem("end");
	}

	private void stopWordsAnalyzer() {
		// parse file and erase stop words
	}

	/* parsing files and erase every whitespace,number,sign */
	private void parse(String directory) throws IOException {
		File dir = new File(directory);

		String delimiter = "\t\n\r\f\b!@#$%^&*;:'\\\".,0123456789()_-[]{}<>?|~`+-=/ \\'«»§΄―—’‘–°·";
		Word word = null;
		int prevPos, nextPos = 0;
		int maxFreq = 0;
		int i =0,wordPos=0;
		
		inputFiles = dir.listFiles();

		for (File f : inputFiles) {
			if (f.isFile()) {
				maxFreq = 0;
				InputStreamReader fileReader = new InputStreamReader(
						new FileInputStream(f),  "UTF8");
				BufferedReader bufReader = new BufferedReader(fileReader);
				StringTokenizer tokenizer = null;
				String token = null, line = null;

				// Create new Document for this file and add it to the list
				Document d = new Document(f.getName(), f.getAbsolutePath());
			
				RandomAccessFile rafFile = new RandomAccessFile(
						f.getAbsolutePath(), "rw");
				
				wordPos = 0;
				while ((line = bufReader.readLine()) != null) {
					i =0;
					
					tokenizer = new StringTokenizer(line, delimiter);

					while (tokenizer.hasMoreTokens()) {
						token = tokenizer.nextToken().toLowerCase();
					
						d.incrementWordsCounter();
						int tempos = wordPos;
						
						for( ;(i<line.length()) && (delimiter.indexOf(line.charAt(i))>-1); i++,wordPos++);
						wordPos += token.length();
						i+= token.length();
						
						token = Stemmer.Stem(token);
						if (!isStopWord(token)) {
							word = new Word(token);
							if (!vocabulary.containsKey(token)) {
								word.addWordFreqMap(d.getDocumentID());

							} else {
								word = vocabulary.get(token);
								word.addWordFreqMap(d.getDocumentID());
							}
							word.addToPosList(d.getDocumentID(), tempos);
						}
						wordPos++;
						
						if (word != null) {
							if (word.getMaxTFDoc().containsKey(
									d.getDocumentID())) {
								if (maxFreq < word.getMaxTF(d.getDocumentID())) {
									maxFreq = word.getMaxTF(d.getDocumentID());
									maxtfdoc.put(d.getDocumentID(),
											(short) maxFreq);
								}
							}
							vocabulary.put(word.getWord(), word);
						}
					}
				}
				bufReader.close();
				docFile.write(Long.toString(d.getDocumentID()).getBytes(
						Charset.forName("UTF-8")));
				docFile.write((" " + d.getDocumentPath()).getBytes(Charset
						.forName("UTF-8")));
				docFile.write((" " + d.getDocumentFormat()).getBytes(Charset
						.forName("UTF-8")));
				docFile.write((" " + d.getWordsCounter()).getBytes(Charset
						.forName("UTF-8")));
				docFile.write(System.getProperty("line.separator").getBytes(
						Charset.forName("UTF-8")));

				prevPos = nextPos;
				nextPos = (int) docFile.length();
				d.setDocsLineSize(nextPos - prevPos);
				d.setDocsLinePos(prevPos);

				docsMap.put(d.getDocumentID(), d);
			} else {
				parse(f.getAbsolutePath());
			}
		}

	}

	/* Returns a list of all the stop words from both GR+EN files */
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

	/* Check if word given is a stopWord */
	boolean isStopWord(String word) {
		if (!stopWords.containsKey(word)) {
			return false;
		} else {
			return true;
		}
	}

	public File[] getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(File[] inputFiles) {
		this.inputFiles = inputFiles;
	}

	public HashMap<String, Integer> getStopWords() {
		return stopWords;
	}

	public HashMap<Long, Document> getDocsList() {
		return docsMap;
	}

	public void setDocsList(HashMap<Long, Document> docsMap) {
		this.docsMap = docsMap;
	}

	public TreeMap<String, Word> getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(TreeMap<String, Word> treeMap) {
		this.vocabulary = treeMap;
	}

	public HashMap<Long, Short> getMaxtfdoc() {
		return maxtfdoc;
	}

	public void setMaxtfdoc(HashMap<Long, Short> maxtfdoc) {
		this.maxtfdoc = maxtfdoc;
	}

}
