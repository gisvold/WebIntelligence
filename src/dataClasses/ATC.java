package dataClasses;

public class ATC {
	
	private String first;
	private String second;
	private String third;
	private String fourth;
	private String fifth;
	
	private String label;
	
	public ATC(String[] code, String label){
		first = code[0];
		second = code[1];
		third = code[2];
		fourth = code[3];
		fifth = code[4];
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getCode(){
		return first+second+third+fourth+fifth;
	}
	
}
