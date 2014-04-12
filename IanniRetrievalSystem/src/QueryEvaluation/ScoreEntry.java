package QueryEvaluation;

/**
 * 
 * Represents class for scores for retrieval models.
 *
 */
public class ScoreEntry {

	private float score;
    private String docID;

    public ScoreEntry(float score, String docID) {
        this.score = score;
        this.docID = docID;
    }

    public String getDocID() {
        return docID;
    }

    public float getScore() {
        return score;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int compare(Object o1, Object o2) {
        ScoreEntry sc1 = (ScoreEntry)o1;
        ScoreEntry sc2 = (ScoreEntry)o2;
        float d1 = sc1.getScore();
        float d2 = sc2.getScore();
        if (d1 - d2 > 0)
            return 1;
        else if (d1 - d2 < 0)
            return -1;
        else
            return 0;
    }

}