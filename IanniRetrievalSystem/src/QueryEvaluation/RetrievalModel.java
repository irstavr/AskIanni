package QueryEvaluation;

import java.io.IOException;
import java.util.LinkedList;

public interface RetrievalModel {
	
	public ScoreEntry[] evaluateQuery(String query) throws IOException;
	
	public void setDocsList(LinkedList<String> docsList);

}
