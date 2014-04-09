package IndexingProcess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

/* actual inverted index object built by the Indexer 
 * loaded from disk by the index loader
 */
public class InvertedIndex implements java.io.Serializable{
	
	private SortedSet<Document> index;	/* actual inverted index - lists of words in the inverted index*/
	private ArrayList<Document> documentList;					/* stores structured info about each doc */

	/* constructor initializes by setting up the Document object 
	 * instantiates a null HashMap, called index.
	 */ 
	InvertedIndex () {
		index = new TreeSet<Document>(new DocComparator());
		documentList = new ArrayList<Document>();		
	}
	/***
	 * TAKE A LOOK 
	 */

	private class DocComparator implements Comparator<Document>
	{
			public int compare(Document o1, Document o2) {
				 return ((o1.getDocumentID() < o2.getDocumentID()) ? -1:(o1.getDocumentID() > o2.getDocumentID())? 1:0);
			}
	}
	
	/* clears InvertedIndex */
	public void clear() {
		index.clear();
	}

	/* Accepts a Document d and adds it to the inverted index */
	public void add(Document d) {
	    
	    index.add(d);
	  
	}

/*	
 
  
 *   words 	 = d.getWords();       			 get current words map   
	    wordsSet = words.keySet();				 get all the words objects

	     loop through the words and add them to the index 
	    Iterator<String> i = wordsSet.iterator(); 
	    while (i.hasNext()) {
	          word = (String) i.next();

	           if we have the word, just get the existing posting list 
	          if (index.containsKey(word)) {
	             postingList = index.get(word); 
	          } else {
	              otherwise, add the word and then create new posting list 
	             postingList = new LinkedList<PostingListNode>(); 	//instantiate a null linked list
	           //  index.put(key, value)(word, postingList); 	//associates the null list with the index HashMap
	          }

	           at this point, the word we are adding
	           * has a postingList associated with it
	           
	       //   LinkedList<Integer> positions = word.getPosList().get(d);

	           now add this word to the posting list  
	        //  tf = (WordFrequency) words.get(word);
	          
	       //   PostingListNode currentNode = new PostingListNode(positions, tf);
	         // postingList.add(currentNode);
	    }*/
	/* Writes the index to disk */
	public void write() {
		FileOutputStream ostream = null; 
		ObjectOutputStream p = null; 
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("1", "1");
		map1.put("2", "3");
		map1.put("3", "3");
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("4", "4");
		map2.put("5", "6");
		HashMap<String, String> map3 = new HashMap<String, String>();
		map3.putAll(map1);
		map3.putAll(map2);
		/* output file location is identified from the configuration file */
		String outputFile = "PostingFile.txt"; 
		
		/* open ObjectOutputStream */
		try {
			ostream = new FileOutputStream(outputFile); 
			p = new ObjectOutputStream(ostream); 
		}
		catch (Exception e) { 
			System.out.println("Can't open output file."); 
			e.printStackTrace(); 
		} 
	 
		/* write object to the disk */
		try {
			p.writeObject(map3);
			p.flush();
			p.close();
			System.out.println("Inverted index written to file ==> "+ outputFile);
		}
		catch (Exception e) {
			System.out.println("Can't write output file.");
			e.printStackTrace();
		}
	}

	
	private void createFilePath()
	{
		File theDir = new File("DocumentsCollection");

		  // if the directory does not exist, create it
		  if (!theDir.exists()) {
		    boolean result = theDir.mkdir();  

		     if(result) {    
		       System.out.println("DIR created");  
		     }
		  }
	}
	/* Returns a posting list for a given word */
	public SortedSet<Document> getPostingList(String word) {
		LinkedList<PostingListNode> result = new LinkedList<PostingListNode>();
		return index;

	/*	if ( index.containsKey(word) ) {
			result = index.get(word);
		} else {
			result = null;
		}
		return result;*/
	}

	/* copies the list of documents into the InvertedIndex Object */
	public void setDocumentList(ArrayList<Document> documentList2) {
		this.documentList = documentList2;
	} 
	
	/* returns the documents list for the index */
	public ArrayList<Document> getDocumentsList() {
		return this.documentList;
	}
	
	/* returns the inverted index */
	public SortedSet<Document> getIndex() {
		return this.index;
	}




	  
}