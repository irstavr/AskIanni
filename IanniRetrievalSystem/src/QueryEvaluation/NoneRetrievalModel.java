package QueryEvaluation;

import java.util.Iterator;
import java.util.LinkedList;

public class NoneRetrievalModel implements RetrievalModel{

	private LinkedList<String> docs;

	public NoneRetrievalModel() {
		this.docs = new LinkedList<String>();
	}

	public ScoreEntry[] evaluateQuery(String query) {
		
		//create new score entries for this list of documents
		ScoreEntry[] results = new ScoreEntry[docs.size()];
        
        Iterator<String> it = ((LinkedList<String>) docs).iterator();
        int i = 0;
        while (it.hasNext()) {
        	results[i] = new ScoreEntry(1, it.next());
        	i++;
        }
        return results;
	}
	
	public void setDocsList(LinkedList<String> docsList) {
		System.out.println("setDocsList sto NoneRetrieavalModel");
		this.docs = docsList;
	}
	
}