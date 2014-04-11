package IndexingProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Document {
	private static long nextID = 1;

	private long documentID;
	private String documentName;
	private String documentPath;
	private String documentFormat;
	private HashMap<String, Word> words;
	private int wordsCounter;
	private double norma;
	private HashMap<String, ArrayList<Integer>> posList;
	private int docsLineSize;
	private int docsLinePos;

	public Document(String name, String path) {
		this.documentID = getNextId(); /* for auto-increment */
		this.documentName = name;
		this.documentPath = path;

		/* Find the format of the Document */
		int pos = name.lastIndexOf('.');
		this.documentFormat = name.substring(pos + 1);

		this.words = new HashMap<String, Word>();
		this.posList = new HashMap<String, ArrayList<Integer>>();
		this.wordsCounter = 0;
		this.norma = 0;
		
		StringBuilder s = new StringBuilder()
		.append(Long.toString(this.documentID)).append("\t")
		.append(this.documentPath).append("\t").append("\t")
		.append(this.getDocumentFormat()).append("\n");
		
		docsLineSize = s.length();
	}

	public void addWord(String word, Word freq) {
		this.words.put(word, freq);
	}

	public String getDocumentFormat() {
		return this.documentFormat;
	}

	public long getDocumentID() {
		return this.documentID;
	}

	public String getDocumentName() {
		return this.documentName;
	}

	public String getDocumentPath() {
		return this.documentPath;
	}

	private synchronized long getNextId() {
		return nextID++;
	}

	public double getNorma() {
		return norma;
	}

	public HashMap<String, ArrayList<Integer>> getPosList() {
		return posList;
	}

	public HashMap<String, Word> getWords() {
		return this.words;
	}

	public int getWordsCounter() {
		return this.wordsCounter;
	}

	public void incrementWordsCounter() {
		this.wordsCounter++;
	}

	public void setDocumentFormat(String format) {
		this.documentFormat = format;
	}

	public void setDocumentID(long id) {
		this.documentID = id;
	}

	public void setDocumentName(String name) {
		this.documentName = name;
	}

	public void setDocumentPath(String path) {
		this.documentPath = path;
	}

	public void setNorma(double norma) {
		this.norma = norma;
	}

	public void setPosList(String word, int pos) {

		// System.out.println("Word : " + word + " Pos: " + pos);
		if (posList.containsKey(word)) {
			ArrayList<Integer> listPos = posList.get(word);
			listPos.add(pos);
			posList.put(word, listPos);
		} else {
			/* otherwise, add the word and then create new posting list */
			ArrayList<Integer> listPos = new ArrayList<Integer>();
			listPos.add(pos);
			posList.put(word, listPos);
		}

		for (String s : posList.keySet()) {
			for (int i = 0; i < posList.get(s).size(); i++)
				;
			// System.out.println("Word : " + s + posList.get(s).get(i));

		}
	}

	public int getDocBytes() {
		return 1;
	}

	public int getDocsLinePos() {
		return docsLinePos;
	}

	public void setDocsLinePos(int docsLinePos) {
		this.docsLinePos = docsLinePos;
	}

	public int getDocsLineSize() {
		
		
		return docsLineSize;
	}

	public void setDocsLineSize(int docsLineSize) {
		this.docsLineSize = docsLineSize;
	}
}