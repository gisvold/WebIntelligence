package dataClasses;

public class TherapyChapter {

	private String therapyChapter;
	private String[] sentences;
	private String text;
	
	public TherapyChapter(String name, String result){
		this.therapyChapter = name;
		this.setSentences(result);
		this.setText(result);
	}
	
	public String getTherapyChapter(){
		return therapyChapter;
	}
	
	public void setTerapyChapter(String therapyChapter){
		this.therapyChapter = therapyChapter;
	}
	
	public String[] getSentences(){
		return sentences;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	private void setSentences(String therapyChapter){
		sentences = therapyChapter.split("\\.");
	}
	
}
