package src.parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import src.dataClasses.ATC;


public class ATCParser {
	
	private ArrayList<ATC> ATCClasses;
	
	public ATCParser(String ATCPath){
		
		ATCClasses = new ArrayList<ATC>();
		
		try{
			
			FileInputStream fileStream = new FileInputStream(ATCPath);
			DataInputStream in = new DataInputStream(fileStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String ATCLine;
			String[] tempArray = new String[5];
			
			while((ATCLine = br.readLine()) != null){
				String[] parsed = codeParser(ATCLine);
				for(int i = 0; i < tempArray.length; i++){
					if(i > parsed.length-1){
						tempArray[i]="";
						
					} else{
						tempArray[i] = parsed[i];
					}
					
					String label = labelParser(ATCLine);
					ATC tempATC = new ATC(tempArray, label);
					ATCClasses.add(tempATC);
				}
				
				in.close();
			}
			
		}catch(Exception e){
				System.err.println("Error: " + e.getMessage());
		}
	}
	
	
	public static String[] codeParser(String prologLine){
		
		String[] resultArray = new String[5];
		String[] ATCCodeArray = prologLine.split("\\[");
		ATCCodeArray = ATCCodeArray[1].split("\\]");
		ATCCodeArray = ATCCodeArray[0].split(",");
		
		return ATCCodeArray;
	}
	
	
	public static String labelParser(String prologLine){
		
		String[] tempArray = prologLine.split("'");
		
		return tempArray[1];
	}
	
	
	public ArrayList<ATC> getATCClasses(){
		return ATCClasses;
	}

}
