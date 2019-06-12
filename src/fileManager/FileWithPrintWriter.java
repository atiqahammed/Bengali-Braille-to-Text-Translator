package fileManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWithPrintWriter {

	private FileWriter fileWriter;
	private PrintWriter printWriter;

	public FileWithPrintWriter(String fileName) throws IOException {
		fileWriter = new FileWriter(fileName);
		printWriter = new PrintWriter(fileWriter);
	}

	public void writeInfile(String text) {
		printWriter.print(text);
	}

	public void writeInfile() {
		printWriter.println();
	}

	public void closeFile() {
		printWriter.close();
	}

}
