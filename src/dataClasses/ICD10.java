package dataClasses;

import java.util.ArrayList;

public class ICD10 {
	
	private String label;
	private String icdCode;
	private ArrayList<String> synonyms;
	
	public ICD10(){
		synonyms = new ArrayList<String>();
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void setIcdCode(String icdCode){
		this.icdCode = icdCode;
	}
	
	public String getIcdCode(){
		return icdCode;
	}
	
	public void addSynonum(String synonym){
		synonyms.add(synonym);
	}

	public ArrayList<String> getSynonyms(){
		return synonyms;
	}
}
