package dataClasses;

public class PatientCase {
	
	private int caseNumber;
	private String[] sentences;
	private int numberOfSentences;
	private String text;
	private String id;
	
	public PatientCase(int caseNumber, String result){
		this.setCaseNumber(caseNumber);
		this.id = "case "+caseNumber;
		this.setNumberOfSentences(sentences.length);
		this.setText(result);
	}
	
	public String[] getWords(){
		String[] words = text.split(" ");
		return words;
	}
	
	private void setSentences(String patientCase){
		sentences = patientCase.split("\\.");
	}
	
	private void setCaseNumber(int caseNumber){
		this.caseNumber = caseNumber;
	}
	
	public int getCaseNumber(){
		return caseNumber;
	}
	
	public String[] getSentence(){
		return sentences;
	}

	private void setNumberOfSentences(int numberOfSentences){
		this.numberOfSentences = numberOfSentences;
	}
	
	public int getNumberOfSentences(){
		return numberOfSentences;
	}
	
	private void setText(String text){
		this.text = text;
	}
	
}
