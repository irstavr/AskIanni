package QueryEvaluation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public interface RetrievalModel {
	
	public HashMap<String,Double> evaluateQuery(String[] terms) throws IOException;
	
	public void setDocsList(Set<String> docsList);

}
