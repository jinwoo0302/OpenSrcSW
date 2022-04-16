import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class searcher {
	@SuppressWarnings({ "null", "unchecked", "rawtypes", "unused" })
	public static double[] InnerProduct(String path, String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		
		
		String qstr = query;
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(qstr, true);
		
		int wordcnt=0;
		String[] word =new String[100000];
		int[] Tf=new int[100000];
		for (int i = 0; i < kl.size(); i++) {
				Keyword kwrd = kl.get(i);
				word[i]= kwrd.getString();
				Tf[i]=kwrd.getCnt();
		

//				System.out.printf("word[%d]:%s \t Tf[%d]:%d\n",i,word[i],i,Tf[i]);
				wordcnt++;
		
		}
		
//		System.out.println("wordcnt:  "+wordcnt);
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		
		document=docBuilder.parse("./collection.xml");
		
		NodeList titles = document.getElementsByTagName("title");
		NodeList nodes = document.getElementsByTagName("body");
//		System.out.println("파싱할 리스트 수 : "+nodes.getLength());
		
	
		
		
		String[] Title=new String[titles.getLength()];
		double[] Total= new double[nodes.getLength()];
		
		for(int i=0;i<titles.getLength();i++){
			Title[i]=titles.item(i).getTextContent();
			Total[i]=0;
//			System.out.printf("Title[%d] : %s\tTotal[%d] : %s\n",i,Title[i],i,Total[i]);
		}
		
		
		
		
		
		
		
		
		//index.post 읽어와서 계산
		FileInputStream fileStream= new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
//		System.out.println("읽어올 객체의 type: "+object.getClass());
		
		HashMap hashMap =(HashMap) object;
		Iterator<String> it=hashMap.keySet().iterator();
		
		
		int cnt=0;
		while(it.hasNext()) {
			String key=it.next();
			String value=(String)hashMap.get(key);

						
			for(int i=0; i<wordcnt;i++) {
				
//				String 비교할 땐 equals!(==는 주소값도 동일해야 함, string은 클래스임)
				if(key.equals(word[i])) {
								
					String[] temp=value.split(" ");
					
//					System.out.println(cnt+"  "+word[i]+"  "+value);
					
					for(int j=0;j<temp.length;j=j+2) {
						int id=Integer.parseInt(temp[j]);
						Total[id]+=Double.parseDouble(temp[j+1])*Tf[i];
						
					}
				}
			}
			
			cnt++;
		}
		


		return Total;
	}


}
