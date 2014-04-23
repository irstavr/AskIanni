package QueryEvaluation;

import java.util.Comparator;

/**
 * 
 * Represents class for scores for retrieval models.
 *
 */
public class ScoreEntry implements Comparable<ScoreEntry> {

	private Double score;
    private String docID;

    public ScoreEntry(Double score, String docID) {
        this.score = score;
        this.docID = docID;
    }

    public String getDocID() {
        return docID;
    }

    public Double getScore() {
        return score;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void setScore(Double score) {
        this.score = score;
    }

  




	@Override
	public int compareTo(ScoreEntry o) {
		// TODO Auto-generated method stub
		return o.getScore().compareTo(this.getScore());
	}

}