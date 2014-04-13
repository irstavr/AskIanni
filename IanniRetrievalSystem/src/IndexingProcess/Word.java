package IndexingProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/* Models the words of a Document */
public class Word {
	private String word;						 				/* value of word */
	private Integer docFreq;						 				/* the sum of Documents that we can find this word in */
	private short maxTF;
	private HashMap<Long,WordFrequency> wordFreqMap;				 				/* the sum of occurrences of this word */
	private ArrayList<Integer> posList;	 						/* the map of Documents that this word exists in and the list of positions in this Document*/
	private HashMap<Long, ArrayList<Integer>> docPosMap;
	private HashMap<Long,Short> maxTFDoc;
	
	Word (String word) {
		this.word 		= word;
		this.docFreq 	= 0;
		this.wordFreqMap = new HashMap<Long,WordFrequency>();
		this.posList 	= new ArrayList<Integer>();
		this.docPosMap = new  HashMap<Long,ArrayList<Integer>>();
		this.maxTFDoc = new  HashMap<Long,Short>();
	}

	/* returns value of word */
	public String getWord() {
		return word;
	}

	/* returns docFreq of this word */
	public Integer getDocFreq() {
		return this.docFreq;
	}

	/* increments docFreq of this word */
	public void incrementDocFreq() {
		this.docFreq++;
	}

	/* return list of docs that this word exists in */
	public ArrayList<Integer> getPosList() {
		return this.posList;
	}
	public void  setPosList(ArrayList<Integer> pos) {
		 this.posList = pos;
	}
	/* add the parameter doc-pos to the map of docs-positions of this word */
	public void addToPosList(Long doc,int pos) {	
		 if (docPosMap.containsKey(doc)) {
			   ArrayList<Integer> listPos = docPosMap.get(doc);
			   listPos.add(pos);
			   docPosMap.put(doc, listPos);
			  } else {
			   /* otherwise, add the word and then create new posting list */
			   ArrayList<Integer> listPos = new ArrayList<Integer>();
			   listPos.add(pos);
			   docPosMap.put(doc, listPos);
			  }	
	}

	public HashMap<Long,WordFrequency> getWordFreqMap() {
		return wordFreqMap;
	}

	public void addWordFreqMap(Long doc) {
		Short maxtf=0;
		
		if(this.wordFreqMap.containsKey(doc))
		{
			this.wordFreqMap.get(doc).increment();
			maxtf = this.wordFreqMap.get(doc).getTF();
			this.maxTFDoc.put(doc, maxtf);
		}
		else
		{
			WordFrequency tf = new WordFrequency();
			tf.increment();
			this.wordFreqMap.put(doc, tf);
			this.maxTFDoc.put(doc, tf.getTF());
		}
		
		
	}

	public HashMap<Long, ArrayList<Integer>> getDocPosMap() {
		return docPosMap;
	}

	public void addDocPosMap(Long doc) {
		this.docPosMap.put(doc, this.posList);
	}

	public short getMaxTF(Long id) {
		return maxTFDoc.get(id);
	}

	public void setMaxTF(short maxTF) {
		this.maxTF = maxTF;
	}

	public HashMap<Long,Short>  getMaxTFDoc() {
		return maxTFDoc;
	}
	public void setMaxTFDoc(HashMap<Long,Short> map) {
		this.maxTFDoc = map;
	}	
	public void MaxTFDoc(long id, short maxTFDoc) {
		this.maxTFDoc.put(id, maxTFDoc);
	}	
}	