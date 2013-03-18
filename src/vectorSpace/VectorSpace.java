package vectorSpace;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class VectorSpace {
	
	HashMap<String, Integer> indexedTerms;
	HashMap<String, Integer> totalFrequencyTerms;
	
	
	public VectorSpace(String[] terms){
		this.indexedTerms = new HashMap<String, Integer>();
	}
	
	public VectorSpace(HashMap<String, Integer> terms){
		this.indexedTerms = terms;
		totalFrequencyTerms = new HashMap<String, Integer>();
	}
	
	public VectorSpace(HashMap<String, Integer> terms, HashMap<String, Integer> termFrequencies){
		this.indexedTerms = terms;
		totalFrequencyTerms = termFrequencies;
	}
	
	private void indexTerms(String[] terms){
		indexedTerms = new HashMap<String, Integer>();
		
		for(int i = 0; i < terms.length; i++){
			indexedTerms.put(terms[i], i);
		}
	}
	
	public double sim(double[] v1, double[] v2){
		double vectorProduct = 0.0;
		double absoluteVector1 = 0.0;
		double absoluteVector2 = 0.0;
		for(int i = 0; i < v2.length; i++){
			vectorProduct += v1[i]*v2[i];
			absoluteVector1+=Math.pow((double) v1[i], 2);
			absoluteVector2+=Math.pow((double) v2[i], 2);
		}
		absoluteVector1 = Math.sqrt(absoluteVector1);
		absoluteVector2 = Math.sqrt(absoluteVector2);
		
		return vectorProduct/(absoluteVector1*absoluteVector2);
		
	}
	
	
	public String stripStopWords(String file){
		System.out.println("Removing stop words");
		try{
			FileInputStream fStream = new FileInputStream("resources/norwegianStopwords.txt");
			DataInputStream in = new DataInputStream(fStream);
			BufferedReader stopwords = new BufferedReader(new InputStreamReader(in));
			String stopwordString;
			while ((stopwordString = stopwords.readLine()) != null){
				file = file.replaceAll(" " + stopwordString + " ", " ");
				file = file.replaceAll(" " +stopwordString, "");
				System.out.println("removed all occurences of ''"+ stopwordString + "''");
			}
			System.out.println(file);
		} catch (FileNotFoundException e) {
			System.out.println("Did not find the file with the stop words");
		
		} catch(IOException e){
			System.out.println("Error reading text");
		}
		
		return file;
	}
	

}
