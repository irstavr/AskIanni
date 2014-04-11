package IndexingProcess;

import java.io.*;

public class WRFile {
	public static void main(String[] args) {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile("rand.txt", "rw");
			// Writing to the file
			file.writeChar('A');
			file.writeChar('B');
			file.writeChar('C');
			file.writeChar('D');
			file.seek(0); // get first item
			System.out.println(file.readChar());
			file.seek(4); // get third item (char size = 2 byte, 2*2)
			System.out.println(file.readChar());
			file.close();
		} catch (Exception e) {
		}
	}
}