package IndexingProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    	
        for (File f : inputFiles) {
        	//RandomAccessFile randomFile = new RandomAccessFile(f.getAbsolutePath(),"rw");
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
        			//### debug print
        			//System.out.println("Token : " + token + "| WordPos : " + wordPos + "|  DocWordsCounter : " + d.getWordsCounter());
        			
        			if(!isStopWord(token)){
        				Word word = isWordOnMapOfWords(token, d.getWords());
  
 //PART1
        				if(word == null)
        				{
        					//System.out.println("Rouggas");
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
        				//### debug print
        				//System.out.println("Token : " + token + " Word Pos : " + wordPos + " DWordsCounter : " + d.getWordsCounter() + " keyword : " + word.getDocFreq());
        				//System.out.println("Size : " + d.getPosList().size());
      
        			
 //PART2
 /******************************************************************************************************************
  * give attention here please		
  */
        				//this iteration updates the docfreq-wordfreq
        				//for our vocabulary and generally from the doclist
        				//we 're going to create our posting file.
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
        				
        				//ind contains the distinct Only Words from all document files. Treemap - String,Word
        				//essentially, represents our VOCABULARY
        				if ( !vocabulary.containsKey(token)){

        					vocabulary.put(token, (int) word.getWordFreq().getTF());
        				}
/**********************************************************************************************************************
 * i think we should change the code to be better and efficient.
 * i believe we should merge the code from parts 1-2
 * 
 * Part2 is related with all docs. 
 * Part1 is related with one doc.
 */
        			}
        			else {
        				System.out.print("Word: "+token+" is a stop word.");
        			}
        			
        		}
        	}
        	bufReader.close();
        	wordPos = 0;
        	
        	docsMap.put(Long.toString(d.getDocumentID()), d);
        }
        System.out.println("Vocabulary size"  + vocabulary.size());
	}

	private Word isWordOnMapOfWords(String token, HashMap<String, Word> hashMap) {
		//if we change HashMap to HashMap<String>, we ll be able to use
		//return words.containsKey(token); for checking if token exists already in map
		//much faster?!

		
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
