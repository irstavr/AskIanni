package QueryEvaluation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class NoneRetrievalModel implements RetrievalModel{

	private Set<String> docs;

	public NoneRetrievalModel() { }

	public HashMap<String,Double> evaluateQuery(String[] token) {		
		//create new score entries for this list of documents
		HashMap<String, Double> scores = new HashMap<String, Double>();
        
        Iterator<String> it = ((Set<String>) docs).iterator();
        while (it.hasNext()) {
        	String id = it.next();
        	scores.put(id, (double) 1);
        }
        
        return scores;
	}
	
	
	@Override
	public void setDocsList(Set<String> docsList) {
		this.docs = docsList;
	}
	
}