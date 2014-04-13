package QueryEvaluation;

/**
 * Represents to every line of the VocabularyFile.txt
 * All the info stored for an exact term.
 *
 */
public class VocabularyEntry {

	private long df;
	private int posStart;
	private int bytesLength;
	
	public VocabularyEntry(long df, int start, int bytesLength) {
		this.setDf(df);
		this.setPosStart(start);
		this.setBytesLength(bytesLength);
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

	public int getBytesLength() {
		return bytesLength;
	}

	public void setBytesLength(int bytesLength) {
		this.bytesLength = bytesLength;
	}
	

}
