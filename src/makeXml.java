import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class makeXml {
	public static void makexml(Document document, String filename) throws FileNotFoundException, TransformerException {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			DOMSource source = new DOMSource((Node) document);
			StreamResult result = new StreamResult(
					new FileOutputStream(new File(filename)));
			transformer.transform(source, result);
	}
	
}
