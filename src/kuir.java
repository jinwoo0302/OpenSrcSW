import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	@SuppressWarnings("static-access")
	public static void main(String[] args)
			throws ParserConfigurationException, IOException, TransformerException, SAXException {
		String command=args[0];
		String path=args[1];
		
		if(command.equals("-c")){
			makeCollection collection = new makeCollection(path);
			collection.makecollection(path);
		}else if(command.equals("-k")) {
			makeKeyword keyword= new makeKeyword(path);
			keyword.makekeyword(path);
		}else if(command.equals("-i")) {
			indexer index = new indexer(path);
			index.makehash(path);
		}else
			System.out.println("Invalid arguments");
		
		for(int i=0; i<args.length;i++) {
			System.out.println(args[i]);
		}
	}
	
}