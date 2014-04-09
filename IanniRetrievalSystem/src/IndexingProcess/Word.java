package IndexingProcess;

import java.util.HashMap;
import java.util.LinkedList;

/* Models the words of a Document */
public class Word {
	private String word;						 				/* value of word */
	private double docFreq;						 				/* the sum of Documents that we can find this word in */
	private WordFrequency wordFreq;				 				/* the sum of occurrences of this word */
	private HashMap<String, LinkedList<Integer>> posList;	 	/* the map of Documents that this word exists in 
																	and the list of positions in this Document*/
	
	Word (String word) {
		this.word 		= word;
		this.docFreq 	= 0;
		this.wordFreq	= new WordFrequency();
		this.posList 	= new HashMap<String,LinkedList<Integer>>();
	}

	/* returns value of word */
	public String getWord() {
		return word;
	}

	/* returns docFreq of this word */
	public double getDocFreq() {
		return this.docFreq;
	}

	/* increments docFreq of this word */
	public void incrementDocFreq() {
		this.docFreq ++;
	}
	
	/* returns WordFreq of this word */
	public WordFrequency getWordFreq() {
		return this.wordFreq;
	}
	
	/* increments wordFreq of this word */
	public void incrementWordFreq() {
		this.wordFreq.increment();
	}

	/* return list of docs that this word exists in */
	public HashMap<String, LinkedList<Integer>> getPosList() {
		return this.posList;
	}

	/* add the parameter doc-pos to the map of docs-positions of this word */
	public void addToPosList(String doc, int pos) {
		
		if (posList.containsKey(doc)) {
			LinkedList<Integer> listPos = posList.get(doc);
			listPos.add(pos);
			posList.put(doc, listPos);
		} else {
			/* otherwise, add the word and then create new posting list */
			LinkedList<Integer> listPos = new LinkedList<Integer>();
			listPos.add(pos);
			posList.put(doc, listPos);
		}
	}	
}	