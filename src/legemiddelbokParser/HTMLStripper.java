package legemiddelbokParser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HTMLStripper {
	

		private File[] files;
		private String URL;
		private String orginalFile = "";
		private String pBoundary = "";
		private String sBoundary = "";
		private XMLParser xmlP;

		


		public HTMLStripper(String filename, XMLParser XMLP, String sBoundary,
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

					new HTMLStripper(files[i].getAbsolutePath(), XMLP, sBoundary,
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
	
	
	
	public ArrayList getMeta(String file){
		
		ArrayList meta = new ArrayList();
		int start = file.indexOf("meta name=\"keywords\" content=\"");
		int end	  = file.indexOf("\">", start);
		String keyWords = file.substring(start + 30, end);
		StringTokenizer st = new StringTokenizer(keyWords, ",");
		while (st.hasMoreTokens()){
			meta.add(st.nextToken());
		}
		
		start = file.indexOf("<meta name=\"description\" content=\"");
		end = file.indexOf("\">", start);
		keyWords = file.substring(start + 31, end);
		StringTokenizer st2 = new StringTokenizer(keyWords, ",");
		
		while (st2.hasMoreTokens()){
			
			meta.add(st.nextToken());
		}
		
		return meta;
	}
	
	
	public void parseFile(String file){
		
		String tagsRemoved = "";
		ArrayList sourceList = searchFrame(file);
		String finishedFile = file.toLowerCase();
		String title = searchTitle(finishedFile);
		String strippedFrame;
		orginalFile = finishedFile;
		tagsRemoved = strip(orginalFile);
		
		//##########################
		// Removing stop words
		tagsRemoved = stopWordStripping(tagsRemoved);
		
		tagsRemoved = stripWhiteSpace(tagsRemoved);
		
		for(int i = 0; i < sourceList.size(); i++){
			
			strippedFrame = parseFrame((String) sourceList.get(i));
			
			tagsRemoved += strippedFrame;
		}
		
		try {
			 xmlP.makeXMLTokenizer(title, URL, tagsRemoved);
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (SecurityException e) {
			e.printStackTrace();
			
		}
		
	}

	
	private String parseFrame(String frame){
		
		String tagsRemovedFromFrame = "";
		
		ArrayList sourceList = searchFrame(frame);
		
		frame = frame.toLowerCase();
		frame = letterStripping(frame);
		frame = stripSpecialChar(frame);
		
		tagsRemovedFromFrame = strip(frame);
		tagsRemovedFromFrame = stripWhiteSpace(tagsRemovedFromFrame);
		
		return tagsRemovedFromFrame;
	}
	
	
	
	public String readFile(File filename) throws IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String file = "";
		String temp;
		while ((temp = in.readLine()) != null){
			file += temp;
			file += " ";
			
		}
		return file;
	}
	
	
	
	
	public String searchTitle(String file){
		
		String title = "";
		int start = file.indexOf("<title>");
		int end = file.lastIndexOf("</title>");
		if (start == -1 || end == -1){
			return title;
			
		} else {
			
			title = file.substring(start +7, end);
			title = letterStripping(title);
			title = stripSpecialChar(title);
			
			return title;
		}
		
	}
	
	
	public ArrayList searchFrame(String file){
		
		if (file == null){
			System.out.println("Filen er NULL");
		}
		
		String frame = "";
		ArrayList frameList = new ArrayList();
		file = file.replaceAll("\\<(FRAME|frame) (SRC|src) =\"(.*?)\"..+?\\>",
				"§" + "$1" + "§L");
		while ((file.indexOf("§")) != -1 && (file.indexOf("§L")) != -1){
			int first = file.indexOf("§");
			
			int last = file.indexOf("§L");
			frame = file.substring(first + 1, last);
			file = file.replaceFirst("§" + frame + "§L", " ");
			if (frame.startsWith("http")){
				frameList.add(frame);
			
			
			} else {
				frame = URL + frame;
				
				frameList.add(frame);
			}
			
			
		}
		
		return frameList;
	}
	
	
	
	public String findSource(String URLForFrame) throws MalformedURLException,
			IOException {
		
		URL frameURL = new URL(URLForFrame);
		
		Object source = frameURL.getContent();
		
		InputStream is = (InputStream) source;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(frameURL.openStream()));
		
		String content = "";
		
		String line = br.readLine();
		
		while(line != null) {
			
			content = content.concat(line);
			
			line = br. readLine();
		}
		
		return content;
	}
	
	
	
	
	public String strip(String file){
		
		file = file.replaceAll("(\\p{space}){2,}", " ");
		
		file = file.replaceAll("(\\p{Space}){2,}", " ");

		// file = file.replaceAll("\\s{2,}", " ");

		file = file.replaceAll("<a href=\"http://(.*?)\".+?/a>", " "
				+ "http://$1" + " ");

		file = file.replaceAll("<a href=\"#(.*?)\".+?/a>", " " + URL + "#$1"
				+ " ");

		file = file.replaceAll("<a href=\"(.*?)\".+?/a>", " " + URL + "$1"
				+ " ");

		file = file.replaceAll("(<script.+?</script>)+", "");

		file = file.replaceAll("<p>", pBoundary);

		file = file.replaceAll("<[^>]*>", " ");

		file = file.replaceAll("</[^>]*>", " ");

		file = file.replaceAll("(<meta.*>)+?", "");

		file = file.replaceAll("#.*\\{[^\\}]*\\}", " ");
		
		file = letterStripping(file);
		
		file = stripSpecialChar(file);
		
		file = file.replaceAll("mailto:(.*?)", "$1");
		
		char[] chars = { ',', ';', '!'};
		
		for (int i = 0; i < chars.length; i++){
			
			file = file.replaceAll((String.valueOf(chars[i])), sBoundary);
		}
		
		file = file.replaceAll("\"", " ");
		
		file = file.replaceAll("\'", " ");
		
		
		return file;
	}
	
	
	
	
	public String stripSpecialChar(String file){
		
		file = file.replaceAll("&nbsp;", " ");
		
		file = file.replaceAll("&lt;", " ");

		file = file.replaceAll("&gt;", " ");

		file = file.replaceAll("&copy;", " ");

		file = file.replaceAll("&#[^;];", " ");

		file = file.replaceAll("&#150", " ");

		file = file.replaceAll("&", " ");

		file = file.replaceAll("#", " ");

		file = file.replaceAll("\\|", " ");

		file = file.replaceAll("-", " ");

		file = file.replaceAll("\"", " ");

		file = file.replaceAll("\\(", " ");

		file = file.replaceAll("\\)", " ");
		
		return file;
		
	}
	
	
	
	
	public String stripWhiteSpace(String file){
		
		char[] list = file.toCharArray();
		
		for(int i = 0; i < (list.length -1); i++){
			
			if(Character.isWhitespace(list[i]) || list[i] == 'S'){
				
				if(Character.isWhitespace(list[i + 1]) || list[i + 1] == 'S'){
					
					list[i] = 'Ø';
					
				}
			}
		}
		
		String newfile = String.valueOf(list);
		
		newfile = newfile.replaceAll("Ø", "");
		
		newfile = newfile.replaceAll("S", " ");
		
		return newfile;
	}
	
	
	/**
	 * Strips the text for Norwegian letters such as 'æ', 'ø' and 'å'
	 * @param file
	 * @return
	 */
	public String letterStripping(String file){
		
		file = file.replaceAll("&aring;", "å");

		file = file.replaceAll("&Aring;", "Å");

		file = file.replaceAll("&oslash;", "ø");

		file = file.replaceAll("&Oslash;", "Ø");

		file = file.replaceAll("&aelig;", "æ");

		file = file.replaceAll("&AElig;", "Æ");
		
		return file;
	}
	
	
	
	public String stopWordStripping(String file){
		
		System.out.println("stopWords!");
		
		try{
			FileInputStream fStream = new FileInputStream("resources/norwegianStopWords.txt");
			DataInputStream in = new DataInputStream(fStream);
			BufferedReader stopWords = new BufferedReader(new InputStreamReader(in));
			
			String stopWordString;
			
			while ((stopWordString = stopWords.readLine()) != null){
				file = file.replaceAll(" " + stopWordString + " ", " ");
				
				System.out.println("removed all occurances of ''" + stopWordString + "''");
			}
			System.out.println(file);

		} catch (FileNotFoundException e){
			System.out.println("stop words file not found");
			
		} catch (IOException e){
			
			System.out.println("error reading line");
		}
		
		return file;
	}
	
	
	public static void main(String[] args){
		
		XMLParser XMLP = new XMLParser(new File("src/legeMiddelBokParser/config.xml"), false);
	}
}
