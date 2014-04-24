package QueryEvaluation;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
        return (HashMap<String, Double>) this.sortByValue(scores);

	}
	
	public  <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
	     List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
		 Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
		         public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
		         {
		             return (o2.getValue()).compareTo( o1.getValue() );
		         }
		     } 
		 );
	     
	     Map<K, V> result = new LinkedHashMap<K, V>();
	     for (Map.Entry<K, V> entry : list) {
	         result.put( entry.getKey(), entry.getValue() );
	     }
	     return result;
	 }
	
	@Override
	public void setDocsList(Set<String> docsList) {
		this.docs = docsList;
	}
	
}