package IndexingProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Document {
	private static long nextID = 1;

	private Long documentID;
	private String documentName;
	private String documentPath;
	private String documentFormat;
	private int wordsCounter;
	private double norma;
	private int docsLineSize;
	private int docsLinePos;

	public Document(String name, String path) {
		this.documentID = getNextId(); /* for auto-increment */
		this.documentName = name;
		this.documentPath = path;

		/* Find the format of the Document */
		int pos = name.lastIndexOf('.');
		this.documentFormat = name.substring(pos + 1);

		this.wordsCounter = 0;
		this.norma = 0;
		
		
	
	}


	public String getDocumentFormat() {
		return this.documentFormat;
	}

	public Long getDocumentID() {
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