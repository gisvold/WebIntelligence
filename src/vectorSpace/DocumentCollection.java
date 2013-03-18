package vectorSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DocumentCollection {
	
	ArrayList<DocumentVector> docs;
	HashMap<String, Double> docsRating;
	String ID;
	
	public DocumentCollection(String id){
		docs = new ArrayList<DocumentVector>();
		docsRating = new HashMap<String, Double>();
		this.ID = id;
	}
	
	
	public void addDocument(DocumentVector vector){
		docs.add(vector);
		vector.addListener(this);
	}
	
	
	public void addRating(String id2, double querySim){
		docsRating.put(id2, querySim);
	}
	
	
	public DocumentVector getById(String id){
		for(int i = 0; i < docs.size(); i++){
			if(docs.get(i).getID().equals(id)){
				return docs.get(i);
			}
		}
		return null;
	}
	
	
	public ArrayList<String> sortedByRating(){
		ArrayList<String> temp = new ArrayList<String>();
		
		for(String key: docsRating.keySet()){
			if (temp.size() == 0){
				temp.add(key);
			
			} else {
				for (int i = 0; i <= temp.size(); i++){
					if(i==temp.size()){
						temp.add(key);
						break;
						
					} else if(docsRating.get(temp.get(i))<docsRating.get(key)){
						temp.add(i, key);
						break;
					}
				}
			}
		}
		return temp;
	}

}
