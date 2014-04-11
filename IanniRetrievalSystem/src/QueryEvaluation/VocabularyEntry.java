package QueryEvaluation;

/**
 * Represents to every line of the VocabularyFile.txt
 * All the info stored for an exact term.
 *
 */
public class VocabularyEntry {

	private long df;
	private int posStart;
	private int posEnd;
	
	public VocabularyEntry(long df, int start, int end) {
		this.setDf(df);
		this.setPosStart(start);
		this.setPosEnd(end);
	}

	public long getDf() {
		return df;
	}

	public void setDf(long df) {
		this.df = df;
	}

	public int getPosStart() {
		return posStart;
	}

	public void setPosStart(int posStart) {
		this.posStart = posStart;
	}

	public int getPosEnd() {
		return posEnd;
	}

	public void setPosEnd(int posEnd) {
		this.posEnd = posEnd;
	}

}
