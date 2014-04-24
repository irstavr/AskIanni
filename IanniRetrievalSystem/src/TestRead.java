import java.io.IOException;
import java.io.RandomAccessFile;


public class TestRead {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		readFromFile("Ulysses.txt", 1282,9);
	}

	private static void readFromFile(String filePath, int position, int size)	throws IOException {
		RandomAccessFile file = new RandomAccessFile("documentCollection/bigger/Ulysses.txt", "rw");
		file.seek(position);
		byte[] bytes = new byte[size];
		file.read(bytes);

		String s = new String(bytes, "UTF-8");
		System.out.println("String here " + s);

		file.close();	
	}
}
