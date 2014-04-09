package IndexingProcess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


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

	private static ArrayList<Document> documentList = new ArrayList<Document>(); 
	private static TreeMap<String, Integer> vocabulary = new TreeMap<String,Integer>();
	
	public static void main (String[] args) throws IOException {			
		//B6. builds PostingFile
		//It follows the parsing, stemming(B3) and analyzing
		//of multiple documents (B2)
		InvertedIndex invertedIndex = buildInvertedIndex();
		
		SortedSet<Document> index =  invertedIndex.getIndex();
		
		//B1. print number of different words of the input files
		//printNumOfDiffWords(index);
		
		//B1. print every different word and its word_freq
		printVocabulary();
		printPostingList(index);
		//B4. build VocabularyFile
		//buildVocabularyFile(index);	
		
		//B5. build DocumentFile
		//buildDocumentFile();
	}
		
	/* Returns the size of keys of the index (sum of diff words) */
	private static void printNumOfDiffWords(HashMap<Word,LinkedList<PostingListNode>> index) {		
		System.out.println("#Num_of_diff_words: "+ index.keySet().size());
	}
	
	/* for every word print the frequency of its occurrences */
	private static void printVocabulary() {
		
		System.out.println("Vocabulary : ");
		for(String str : vocabulary.keySet())
		{
			System.out.println("Str : " + str + "\t\t\t | Freq : " + vocabulary.get(str) );
		}

	}
	
	private static void printPostingList(SortedSet<Document> index)
	{
		System.out.println("\nPosting file : ");
		for(String word : vocabulary.keySet())
		{
			for(Document doc : index)
			{
			
				if(doc.getPosList().containsKey(word))
				{
					System.out.print("Doc : " + doc.getDocumentID());
					System.out.print("\t Word : " + word);
					System.out.print("\t Position List size : " + doc.getPosList().get(word).size() + "\t\t[");
					for(int i = 0; i < doc.getPosList().get(word).size(); i++)
					{
						System.out.print(" i : " + doc.getPosList().get(word).get(i) +"   ");
					}
					System.out.println("]\t");
				}
			}
			System.out.println("");
		}
	}
	/* builds the DocumentFile.txt that contains
	 * for every Document its id,path,format,norma
	 */
	private static void buildDocumentFile() {
		//we save every of this info on Document
		//we just parse documentList ;)
		//bingo
	}

	/* builds the VocabularyFile.txt that contains
	 * every Word, the doc_freq and point_to_index.
	 */
	private static void buildVocabularyFile(HashMap<Word,LinkedList<PostingListNode>> index) {
		// exoume hdh oles tis diaforetikes lexeis sto postingFile
		// me ena iteration exoume k to doc_freq k to pos sto postingFile
		// polu eukola
	
		//se auxousa seira
	}

	/* Builds the inverted index, reads all documents */ 
	 public static InvertedIndex buildInvertedIndex () throws IOException {	 
		Document d;
		
		/* Input Files */
		File dir = new File("documentCollection");
        File[] inputFiles = dir.listFiles();
            
		InvertedIndex index = new InvertedIndex(); 
		index.clear();
		
		/* now lets parse the input file
		 * we call the parser that returns a set of Document objects
		 */
	
		Parser parser = new Parser(inputFiles);
		
		
		vocabulary = parser.readDocuments( vocabulary);
		///////////////////////
		
		
		documentList =  parser.getDocsList();
			
		/* put the document list into the InvertedIndex */
		index.setDocumentList(documentList);
		 
		/* we iterate all the Documents that we parsed and
		 * call the 'add' method to add them to the InvertedIndex.
		 */
		
		Iterator<Document> i = documentList.iterator();
		System.out.println("Starting to build the index");
		
		while ( i.hasNext() ) { 
			 d = (Document) i.next(); 
			// System.out.println("Document : " + d.getDocumentID() + " |Words : " + d.getWords().size());
			//this is your INDEX EIRINI :P 
			// System.out.println("Document i :" + d.getDocumentID() + " Words Count" + d.getWords().size());
		 	index.add(d); 
		} 

		
		//PRINT IND TREEMAP - VOCABULARY
	
		
		/* store InvertedIndex to disk */
		index.write(); 		// write the index to a file
		 
		return index; 
	 }
}