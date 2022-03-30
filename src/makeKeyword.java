import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class makeKeyword {
	public makeKeyword(String path, String name) throws TransformerException, ParserConfigurationException, SAXException, IOException {
		makekeyword(path,name);
	}

	public void makekeyword(String filepath, String name) throws TransformerException, ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		
		document=docBuilder.parse(filepath);
		
		NodeList nodes = document.getElementsByTagName("body");
		System.out.println("파싱할 리스트 수 : "+nodes.getLength());

	for(int i = 0;i<nodes.getLength();i++)
	{
		String str1 = nodes.item(i).getTextContent();
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(str1, true);

		String str2 = "";
		for (int j = 0; j < kl.size(); j++) {
			Keyword kwrd = kl.get(j);
			str2 += kwrd.getString() + ":" + kwrd.getCnt() + "#";
		}
		document.getElementsByTagName("body").item(i).setTextContent(str2);
	}

	makeXml.makexml(document,name);
	
	}
}
