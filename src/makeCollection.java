import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {

	public makeCollection(String path) throws ParserConfigurationException, IOException, TransformerException {
		makecollection(path);
	}

	public static File[] makeFileList(String htmlpath) {
		File dir = new File(htmlpath);
		return dir.listFiles();
	}

	public void makecollection(String filename) throws ParserConfigurationException, IOException, TransformerException {


		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();

		Element docs = document.createElement("docs");
		document.appendChild(docs);

		File[] files = makeFileList(filename);

		for (int i = -0; i < 5; i++) {
			org.jsoup.nodes.Document html = Jsoup.parse(files[i], "UTF-8");
			String titleData = html.title();
			String bodyData = html.body().text();

		
			org.w3c.dom.Element doc = document.createElement("doc");
			docs.appendChild(doc);
			doc.setAttribute("id", Integer.toString(i));

			Element title = document.createElement("title");
			title.appendChild(document.createTextNode(titleData));
			doc.appendChild(title);

			Element body = document.createElement("body");
			body.appendChild(document.createTextNode(bodyData));
			doc.appendChild(body);
		}
		
		makeXml.makexml(document,filename);

	}

}
