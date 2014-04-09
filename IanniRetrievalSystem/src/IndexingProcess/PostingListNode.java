package IndexingProcess;

import java.util.LinkedList;

/* This will store and retrieve an entry in a posting list 
 * typically an entry in a posting list contains the document identifier
 * and the term frequency
 */
public class PostingListNode {

	LinkedList<Integer> positions;
	WordFrequency tf;	/* it is a separated object in order to handle the case
						   when we have more than 32767 occurrences of a term */

	public PostingListNode(LinkedList<Integer> position, WordFrequency tf) {
		this.positions = position;
		this.tf = tf;
	}

	public LinkedList<Integer> getPosition() { 
        return positions; 
    }

    public short getTF() { 
        return tf.getTF(); 
    } 
}