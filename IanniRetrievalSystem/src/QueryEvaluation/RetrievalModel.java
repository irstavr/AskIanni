package QueryEvaluation;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface RetrievalModel {
	
	public Map<String,Double> evaluateQuery(String[] terms) throws IOException;
	
	public void setDocsList(Set<String> docsList);

}
