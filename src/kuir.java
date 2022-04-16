import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	@SuppressWarnings("static-access")
	public static void main(String[] args)
			throws ParserConfigurationException, IOException, TransformerException, SAXException, ClassNotFoundException {
		String command=args[0];
		String path=args[1];
		
		if(command.equals("-c")){
			makeCollection collection = new makeCollection(path, "collection.xml");
			collection.makecollection(path, "collection.xml");
		}else if(command.equals("-k")) {
			makeKeyword keyword= new makeKeyword(path, "index.xml");
			keyword.makekeyword(path, "index.xml");
		}else if(command.equals("-i")) {
			indexer index = new indexer(path, "index.post");
			index.makehash(path,"index.post");
		}else if(command.equals("-s")) {
			//post를 path로 참조
			searcher search = new searcher();
			search.InnerProduct(path, args[3]);
		}else
			System.out.println("Invalid arguments");
	}
	
}