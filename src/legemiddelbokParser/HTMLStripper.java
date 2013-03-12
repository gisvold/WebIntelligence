package legemiddelbokParser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class HTMLStripper {
	
	public class HtmlStripper {

		private File[] files;
		private String URL;
		private String orginalFile = "";
		private String pBoundary = "";
		private String sBoundary = "";
		private XMLParser xmlP;

		
		public HtmlStripper(String URL, String sBoundary, String pBoundary,
				String allsmall, XMLParser xmlP) {

			this.URL = URL;
			this.pBoundary = pBoundary;
			this.sBoundary = sBoundary;
			this.xmlP = xmlP;

			try {

				orginalFile = findSource(URL);

			} catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}

			parseFile(orginalFile);

		}


		public HtmlStripper(String filename, XMLParser XMLP, String sBoundary,
				String pBoundary) {

			File file = new File(filename);

			this.URL = file.getName();

			this.xmlP = XMLP;

			this.pBoundary = pBoundary;

			this.sBoundary = sBoundary;

			if (file.isDirectory()) {

				System.out.println("Filen er et directory");

				files = file.listFiles();

				for (int i = 0; i < files.length; i++) {

					new HtmlStripper(files[i].getAbsolutePath(), XMLP, sBoundary,
							pBoundary);

				}

			}

			else {

				System.out.println("Filen er en fil");

				try {

					orginalFile = readFile(file);

				} catch (IOException e) {

					e.printStackTrace();

				}

				parseFile(orginalFile);

			}
		}
	}
	
	
	public ArrayList getMeta(String file){
		
		ArrayList meta = new ArrayList();
		
		
		return meta;
	}
	
	
	public void parseFile(String file){
		
		
	}

	
	private String parseFrame(String frame){
		
		String tagsRemovedFromFrame = "";
		
		return tagsRemovedFromFrame;
	}
	
	public String readFile(File filename) throws IOException {
		
		String file = "";
		
		return file;
	}
	
	
	
	public String searchTitle(String file){
		
		String title = "";
		
		return title;
	}
	
	
	public ArrayList searchFrame(String file){
		
		
		ArrayList frameList = new ArrayList();
		
		
		return frameList;
	}
	
	
	public String findSource(String URLForFrame) throws MalformedURLException,
			IOException {
		
		
		String content = "";
		
		
		return content;
	}
	
	
	public String strip(String file){
		
		file = file.replaceAll("(\\p{space}){2,}", " ");
		
		
		return file;
	}
	
	
	public String stripSpecialChar(String file){
		
		file = file.replaceAll("&nbsp;", " ");
		
		return file;
		
	}
	
	
	public String stripWhiteSpace(String file){
		
		char[] list = file.toCharArray();
		
		String newfile = String.valueOf(list);
		
		newfile = newfile.replaceAll("Ø", "");
		
		newfile = newfile.replaceAll("S", " ");
		
		return newfile;
	}
	
	public String letterStripping(String file){
		
		return file;
	}
	
	
	
	public String stopWordStripping(String file){
		
		return file;
	}
	
	
	public static void main(String[] args){
		
		XMLParser XMLP = new XMLParser(new File("src/legeMiddelBokParser/config.xml"), false);
	}
}
