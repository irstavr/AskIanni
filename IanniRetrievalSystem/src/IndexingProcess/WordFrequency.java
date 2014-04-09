package IndexingProcess;

/*
 * It is a separated object in order to handle the case
 * when we have more than 32767 occurrences of a term
 */
public class WordFrequency {

	  private static final short MAX_VALUE = 32767;
	  private short tf;

	  public WordFrequency() {
	      tf = 0;
	  }

	  /* increment the tf as long as we have room for an increment */ 
	  public void increment () {
	      if (tf <= MAX_VALUE) {
	          tf = (short) (tf + 1);
	      }
	  }

	  public void setTF (short value) { 
	      tf = value; 
	  }
	  
	  public short getTF () { 
	      return tf; 
	  }
}