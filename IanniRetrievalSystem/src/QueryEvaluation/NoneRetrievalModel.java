package QueryEvaluation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class NoneRetrievalModel implements RetrievalModel{

	private Set<String> docs;

	public NoneRetrievalModel() {
	}

	public HashMap<String,ScoreEntry> evaluateQuery(String query) {
		
		//create new score entries for this list of documents
		HashMap<String, ScoreEntry> scores = new HashMap<String, ScoreEntry>();
        
        Iterator<String> it = ((Set<String>) docs).iterator();
        while (it.hasNext()) {
        	String id = it.next();
        	scores.put(id, new ScoreEntry(1, id));
        }
        return scores;
	}
	
	@Override
	public void setDocsList(Set<String> docsList) {
		System.out.println("setDocsList sto NoneRetrieavalModel");
		this.docs = docsList;
	}
	
}