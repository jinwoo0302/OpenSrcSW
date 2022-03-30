import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {
	private static NodeList nodes;
	private static HashMap<String, String> Word;
	private static String[] wordlist;

	


	public indexer(String path, String name) throws IOException, ParserConfigurationException, SAXException {
		makehash(path, name);
		
	}



	@SuppressWarnings({ "unchecked", "rawtypes" , "null"})
	public static void makehash(String filepath, String name) throws IOException, ParserConfigurationException, SAXException {
		
		FileOutputStream fileStream = new FileOutputStream(name);
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		Word = new HashMap();
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		document=docBuilder.parse(filepath);
	
		nodes = document.getElementsByTagName("body");
		

		int cnt=0;
		wordlist = new String[100000];
		
		for(int i = 0;i<nodes.getLength();i++)
		{
			String str = nodes.item(i).getTextContent();
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(str, true);

			for (int j = 0; j < kl.size(); j++) {
				wordlist[cnt] +=kl.get(j);
				cnt++;
			}
		}
	
		
		
		
		
		for(int i = 0; i < nodes.getLength(); i++){
            String str= nodes.item(i).getTextContent();
            Weight(i, str);
		}

		
		objectOutputStream.close();
	}

	
	

	public static void Weight(int id, String str) {
		String word;
		int freq;
		String[] wordfreq = str.split("#");
		for (String str2 : wordfreq) {
			if (str2.equals(""))
				break;
			String[] str3 = str2.split(":");
			word = str3[0];
			freq = Integer.parseInt(str3[1]);
					
			Store(id, word, freq);
		}
	}


	public static void Store(int id, String word, int freq) {
		double weight;
		weight=freq*Math.log(nodes.getLength()/Count(word));
		weight=Math.round((weight*100)/100.0);
		String value;
		value=Word.get(word)+ " "+ id + " "+ weight;
		Word.put(word,value);
	}





	public static int Count(String word) {
		int count=0;
		for(String str : wordlist) {
			if(str.equals(""))
				break;
			if(str.equals(word)) {
					count++;
			}
				
		}
		return count;
	}
		
			
}
