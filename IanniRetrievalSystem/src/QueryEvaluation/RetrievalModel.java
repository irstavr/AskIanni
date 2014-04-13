package QueryEvaluation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public interface RetrievalModel {
	
	public HashMap<String,ScoreEntry> evaluateQuery(String query) throws IOException;
	
	public void setDocsList(Set<String> docsList);

}
