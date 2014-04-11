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

import mitos.stemmer.Stemmer;

/*
 * Parser returns a list of documents, so when a Parser returns,
 * its results are sent to the index builder.
 */
public class Parser {
	private static final String DOCUMENT = "CollectionIndex\\DocumentsFile.txt";
	private HashMap<String,Document> docsMap;
	private TreeMap<String, Integer> vocabulary;
	private File[] inputFiles;
	private HashMap<String,Integer> stopWords;
	private final static Charset ENCODING = StandardCharsets.UTF_8;  


	/* accepts as a parameter the file to parse and the stop words to use */
	public Parser(File[] inputFiles) throws IOException {
		this.docsMap   = new HashMap<String, Document>();
		this.inputFiles = inputFiles;
		this.stopWords  = readStopWordFiles();
		this.setVocabulary(new TreeMap<String, Integer>());
	}

	/* read and parse, analyze, stem the documents */
	public  void readDocuments() throws IOException {
		//parsing file
		parse();
		//stop words analyzer
		stopWordsAnalyzer();
		//run stemmer
		runStemmer();
	

	
	}

	private void runStemmer() {
		Stemmer.Initialize();
		//px
		Stemmer.Stem("end");
	}

	private void stopWordsAnalyzer() {
		//parse file and erase stop words
	}

	/* parsing files and erase every whitespace,number,sign */	
	private  void parse() throws IOException {
    	String delimiter = " ~\t\n\r\f1234567890";
		//String delimiter = "\t\n\r\f!@#$%^&*;:'\".,0123456789()_-[]{}<>?|~`+-=/ \'\b«»§΄―—’‘–°· \\";
    	int prevPos,nextPos = 0;
    	RandomAccessFile docFile = new RandomAccessFile(DOCUMENT, "rw");
        for (File f : inputFiles) {
        
        	InputStreamReader fileReader = new InputStreamReader(new FileInputStream(f), ENCODING.name());   	
        	BufferedReader bufReader = new BufferedReader(fileReader);
        	StringTokenizer tokenizer = null;
			String token = null, line = null;
			Integer wordPos = new Integer(0);

			System.out.println("File: "+ f.getAbsolutePath());

			//Create new Document for this file and add it to the list
			Document d = new Document(f.getName(), f.getAbsolutePath());
			// Start looping through the file
        	while ( (line = bufReader.readLine()) != null) {
        		tokenizer = new StringTokenizer(line, delimiter);

        		while (tokenizer.hasMoreTokens()) {
        			token = tokenizer.nextToken();
        			
        			wordPos++;
        			// increment wordsCounter of this Document
        			d.incrementWordsCounter();
        	
        			if(!isStopWord(token)){
        				Word word = isWordOnMapOfWords(token, d.getWords());

        				if(word == null)
        				{
        					word = new Word(token);
	        				word.incrementDocFreq();
	        				word.incrementWordFreq();
	        				ArrayList<Integer> positions = d.getPosList().get(token);
	        				if ( positions == null ) {
            					positions = new ArrayList<Integer>();
            				}
            				positions.add(wordPos.intValue());
            				d.setPosList(token, wordPos);
	        				d.addWord(token, word);
        				}
        				else{
        					word.incrementWordFreq();
        					ArrayList<Integer> positions = d.getPosList().get(token);
	        				if ( positions == null ) {
            					positions = new ArrayList<Integer>();
            				}
            				d.setPosList(token, wordPos);
        				}

        				HashMap<String,Word> docWords = null;
        				for(String tdoc : docsMap.keySet())
        				{
        					docWords =  docsMap.get(tdoc).getWords();
        					if(docWords.containsKey(token)){
        						Word docWord = docWords.get(token);
        						docWord.incrementDocFreq();
        						docWord.incrementWordFreq();	
        					}
        				}
        				if ( !vocabulary.containsKey(token)){

        					vocabulary.put(token, (int) word.getWordFreq().getTF());
        				}
        			}
        			else {
        				System.out.print("Word: "+token+" is a stop word.");
        			}
        		}
        	}
        	bufReader.close();
        	wordPos = 0;
        	docFile.write(Long.toString(d.getDocumentID()).getBytes(Charset.forName("UTF-8")));
        	docFile.write((" " + d.getDocumentPath() ).getBytes(Charset.forName("UTF-8")));
        	docFile.write((" " + d.getDocumentFormat()).getBytes(Charset.forName("UTF-8")));
        	docFile.write(System.getProperty("line.separator").getBytes(Charset.forName("UTF-8")));
        	prevPos = nextPos;
			nextPos = (int) docFile.length();	
        	d.setDocsLineSize(nextPos-prevPos);
        	d.setDocsLinePos(prevPos);
        	
        	System.out.println("PrevPos : " + prevPos + " NextPos : " +  (nextPos-prevPos));
        	docsMap.put(Long.toString(d.getDocumentID()), d);
        }
  
        docFile.close();
	}

	private Word isWordOnMapOfWords(String token, HashMap<String, Word> hashMap) {
		return (hashMap.containsKey(token)) ? hashMap.get(token) : null; 
	}

	/* Returns a list of all the stop words from both GR+EN files */
	private static HashMap<String, Integer> readStopWordFiles() throws IOException {
		FileReader fileReaderGr = new FileReader("stopwordsGr.txt");
		FileReader fileReaderEn = new FileReader("stopwordsEn.txt");

        BufferedReader bufferedReaderGr = new BufferedReader(fileReaderGr);
        BufferedReader bufferedReaderEn = new BufferedReader(fileReaderEn);
        
        HashMap<String,Integer> stopWords = new HashMap<String,Integer>();
        String stopWordGr, stopWordEn = null;
        
        // parse stopWordsGr
        while ( (stopWordGr = bufferedReaderGr.readLine()) != null ) {
        	stopWords.put(stopWordGr, stopWordGr.length());		//add stop word to list
        }
        bufferedReaderGr.close();
        
        // parse stopWordsEn
        while ( (stopWordEn = bufferedReaderEn.readLine()) != null ) {
        	stopWords.put(stopWordEn, stopWordEn.length());		//add stop word to list
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
	
	
	public HashMap<String, Document> getDocsList() {
		return docsMap;
	}

	public void setDocsList(HashMap<String, Document> docsMap) {
		this.docsMap = docsMap;
	}

	public TreeMap<String, Integer> getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(TreeMap<String, Integer> vocabulary) {
		this.vocabulary = vocabulary;
	}

	
}
