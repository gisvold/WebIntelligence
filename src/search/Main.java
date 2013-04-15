package src.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import org.xml.sax.SAXException;

import src.parser.TherapyReader;


public class Main {
	
	public void makeFile(File inputFile, String output) throws FileNotFoundException{
		String fileContents = "";
		try {
			fileContents = readFile(inputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(new FileOutputStream(new File(output)));
		out.println(fileContents);
		out.println();
		out.flush();
		out.close();
	}
	
	
	public String readFile(File filename) throws IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String file = "";
		String temp;
		while ((temp = in.readLine()) != null) {
			file += temp;
			file += " ";
		}
		return file;
	}
	
	
	public static void main(String[] args) throws IOException, ParseException, SAXException{
		TherapyReader tr = new TherapyReader();
		System.out.println(tr.getChapters().get(0).getText());
	}

}
