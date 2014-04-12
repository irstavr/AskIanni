package QueryEvaluation;

import java.awt.List;
import java.util.HashMap;
import java.util.Iterator;

import IndexingProcess.Document;

public class NoneRetrievalModel implements RetrievalModel {

	private HashMap<String, VocabularyEntry> voc;

	public NoneRetrievalModel(HashMap<String, VocabularyEntry> voc) {
		this.voc = voc;
	}

	@Override
	public ScoreEntry[] evaluateQuery(String query) {
		//Get the docs for this text from PostingFile
		//List docs = voc.getTermDocuments(query);
		
		//create new score entries for this list of docs
        //ScoreEntry[] results = new ScoreEntry[docs.size()];
//        
//      Iterator it = ((Object) docs).iterator();
//      int i = 0;
//      while (it.hasNext()) {
//          results[i] = new ScoreEntry(1, ((Document)it.next()).getDocumentID());
//      	i++;
//      }
//      return results;
		return null;
	}	
}