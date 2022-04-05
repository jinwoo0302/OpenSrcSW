import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer {
	private static NodeList nodes;
	private static HashMap<String, String> Word;
	private static String[] wordlist;
	private static int cnt;

	


	public indexer(String path, String name) throws IOException, ParserConfigurationException, SAXException {
		makehash(path, name);
		
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void makehash(String filepath, String name) throws IOException, ParserConfigurationException, SAXException {
		
		FileOutputStream fileStream = new FileOutputStream(name);
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		Word = new HashMap();
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		document=docBuilder.parse(filepath);
	
		nodes = document.getElementsByTagName("body");
		

		
		
		//wordlist는 모든 단어들을 중복을 허용시켜 
		//Count에서 써먹음(중복 허용 이유: 전체 문서에서 몇 번 나오는지 보려고)
		cnt=0;
		wordlist = new String[100000];
				
		for(int i = 0;i<nodes.getLength();i++)
		{
			String str = nodes.item(i).getTextContent();
			String[] str2 = str.split("#");
			for(int j=0;j<str2.length;j++) {
				String str3=str2[j];
				String[] temp=str3.split(":");
				wordlist[cnt]=temp[0];
				cnt++;
			}
		}
		
		
		for(int i = 0; i < nodes.getLength(); i++){
            String str= nodes.item(i).getTextContent();
            Weight(i, str);
		}

		Iterator<String> it=Word.keySet().iterator();
				
		int cnt=0;
		while(it.hasNext()) {
			String key=it.next();
			String value=(String)Word.get(key);
			
			System.out.println(cnt+"   "+key+"    "+value);
			cnt++;
		}
		
//		for(int i=0;i<cnt;i++) {
//			System.out.println(i+" "+wordlist[i]+" "+Word.get(wordlist[i]));
//		}
//				
		
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
		
		if(Word.get(word)==null) {
			Word.put(word,"");
		}
		
		weight=freq*Math.log((double)nodes.getLength()/(double)Count(word));
		String round = Word.get(word) + id + " "+ String.format("%.2f", weight)+" ";
		
		Word.put(word,round);
	}





	public static int Count(String word) {
		int count=0;
//		System.out.println(word);
		for(int i=0; i<cnt;i++) {
			if(wordlist[i].equals(word)) {
					count++;
			}
		}
//		System.out.println(count);
		return count;
	}
		
			
}
