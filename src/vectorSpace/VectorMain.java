package vectorSpace;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class VectorMain {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, DocumentCollection> collections = new HashMap<String, DocumentCollection>();
		
		
		//terms list:
		HashMap<String, Integer> terms = new HashMap<String, Integer>();
		HashMap<String, Integer> termsTotalCount  = new HashMap<String, Integer>();
		HashMap<String, Double> idfCount = new HashMap<String, Double>();
		int indexCounter =0;

/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////Therapy Chapters///////////////////////////////////////////////////
		
		//Therapy chapters in NLH + generating of terms hash:
		//TODO dette er en copy-paste av pasient-case-lesingen:
		DocumentCollection temp2 = new DocumentCollection("therapy-chapters");
//		System.out.println("Therapy chapters");
		File file = new File("resources/NLH/done/T");
		File[] files = file.listFiles();
		
		SAXBuilder builder = new SAXBuilder();
		
		
		try {
			for (int i = 1; i < files.length; i++) {
				Document tempDoc = builder.build(files[i]);
				Element root = tempDoc.getRootElement();
				String body = root.getChild("doc").getChild("body").getText();
//				System.out.println("##############################BODY##########################");
				body = body.replaceAll("\\n\\.", "");
				body = body.replaceAll("\\.", "");
//				System.out.println(body);
//				System.out.println("##############################first element#########----");
				String[] bodyArr = body.split("\\n");
				//henter ut document id
				String id = root.getChild("doc").getAttributeValue("id").replaceAll("\\.htm", "");
//				System.out.println(id+"---------------------------------");
				DocumentVector v = new DocumentVector(id, bodyArr);
				temp2.addDocument(v);
				
				HashMap<String, Boolean> docContains = new HashMap<String, Boolean>(); 
				
				//update terms list and terms count
				for (int j = 0; j < bodyArr.length; j++) {
					docContains.put(bodyArr[j], true);
					if (terms.containsKey(bodyArr[j])) {
						int tmp = termsTotalCount.get(bodyArr[j]);
						tmp++;
						termsTotalCount.put(bodyArr[j], tmp);
					}
					else{
						terms.put(bodyArr[j], indexCounter);
						indexCounter++;
						termsTotalCount.put(bodyArr[j], 1);

					}
				}
				for(String key : docContains.keySet()){
					if (idfCount.containsKey(key)) {
						double idftemp = (double)idfCount.get(key);
						idftemp= idftemp+1;
						idfCount.put(key, idftemp);
					} else {
						idfCount.put(key, 1.0);
					}
				}
			}
			
			
			
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(String key : terms.keySet()){
			if (!idfCount.containsKey(key)) {
				idfCount.put(key, 0.0);
			}
		}
		for(String key : idfCount.keySet()){
			double idf =idfCount.get(key)/temp2.docs.size();
//			System.out.println(idfCount.get(key));
			idfCount.put(key, (double)idfCount.get(key)/(double)temp2.docs.size());
		}
		System.out.println("terms size: "+terms.size()+"   idf size: "+idfCount.size());
		//generate vectors for therapy docs:
		for (int i = 0; i < temp2.docs.size(); i++) {
			temp2.docs.get(i).generateVector(terms, idfCount);
		}
		
		//add doc collection to set of collections:
		collections.put(temp2.ID, temp2);
		

/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////PASIENT CASES//////////////////////////////////////////////////////
		
		//read pasient-cases
//		System.out.println("pasient-cases");
		DocumentCollection temp = new DocumentCollection("pasient-cases");
		for (int i = 1; i < 9; i++) {
			
//			System.out.println("pasient-cases/stemmed/case"+i+"stemmed.txt:  ########################");
			String strLine = "";
			ArrayList<String> currentDoc = new ArrayList<String>();
			try {
				FileInputStream fstream = new FileInputStream("pasient-cases/stemmed/case"+i+"stemmed.txt");
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				//Read File Line By Line
				while ((strLine = br.readLine()) != null) {
//					System.out.println(strLine);
					strLine = strLine.replaceAll("\\.", "");
					strLine = strLine.replaceAll("\\,", "");
//					System.out.println(strLine);
					currentDoc.add(strLine);

				}
				
			} catch (Exception e) {
				System.out.println("fileread error "+strLine+"  "+e.toString());
			}
			String[] tempArr = new String[currentDoc.size()];
			for (int j = 0; j < tempArr.length; j++) {
				tempArr[j] = currentDoc.get(j);
			}
			temp.addDocument(new DocumentVector("case"+i, tempArr, terms, idfCount));
			

		}
		collections.put(temp.ID, temp);
		
		
//		//printer ut termsList:
//		System.out.println("printing terms, total number of terms is "+terms.size()+":");
//		System.out.println();
//		for (String key : terms.keySet()){
//			System.out.println(key);
//			
//		}
		System.out.println("printing terms, total number of terms is "+terms.size()+":");
		VectorSpace vs = new VectorSpace(terms);
		
		DocumentCollection therapies = collections.get("therapy-chapters");
		

		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("vector_result_idf.txt"));

			for (int i = 0; i < 8; i++) {
				
				DocumentVector case1 = collections.get("pasient-cases").docs.get(i);
				System.out.println(collections.get("pasient-cases").docs.size());
				DocumentVector bestMatch = case1;
				double bestValue = 0.0;
				for (int j = 0; j < therapies.docs.size(); j++) {
					DocumentVector current = therapies.docs.get(j);
					current.setQuerySim(vs.sim(current.getVector(),
							case1.getVector()));
					if (current.getQuerySim() > bestValue) {
						bestMatch = current;
						bestValue = current.getQuerySim();
					}
				}
				//print case result
				ArrayList<String> sorted = therapies.sortedByRating();
				out.println("10 most relevant chapters for "+case1.getID()+" in sorted order:");
				for (int j = 0; j < 10; j++) {
//					System.out.println(sorted.get(i));
					out.println(sorted.get(j));
				}
				out.println();
				out.println();
				
			}
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
