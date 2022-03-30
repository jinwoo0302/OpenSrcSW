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

	public indexer(String path, String name) throws IOException, ParserConfigurationException, SAXException {
		makehash(path, name);
		
	}



	@SuppressWarnings({ "unchecked", "rawtypes" ,"nls"})
	public static void makehash(String filepath, String name) throws IOException, ParserConfigurationException, SAXException {
		
		FileOutputStream fileStream = new FileOutputStream(name);
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		Word = new HashMap();
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		document=docBuilder.parse(filepath);
	
		nodes = document.getElementsByTagName("body");
		
		for(int i = 0; i < nodes.getLength(); i++){
            String str= nodes.item(i).getTextContent();
            Weight(i, str);
		}

		objectOutputStream.writeObject(Word);
		
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




	@SuppressWarnings("unlikely-arg-type")
	public static int Count(String word) {
		int count=0;
		for(int i = 0; i < nodes.getLength(); i++){
			String str1 = nodes.item(i).getTextContent();
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(str1, true);

			for (int j = 0; j < kl.size(); j++) {
				if(word.equals(kl.get(j)) ) {
					count++;
					break;
				}
			}
		}
		
		return count;
		
			
	}
}