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
	public static void CalcSim(String path, String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		
		
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
		

				System.out.printf("word[%d]:%s \t Tf[%d]:%d\n",i,word[i],i,Tf[i]);
				wordcnt++;
		
		}
		
		System.out.println("wordcnt:  "+wordcnt);
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		
		document=docBuilder.parse("./collection.xml");
		
		NodeList titles = document.getElementsByTagName("title");
		NodeList nodes = document.getElementsByTagName("body");
		System.out.println("파싱할 리스트 수 : "+nodes.getLength());
		
	
		
		
		String[] Title=new String[titles.getLength()];
		double[] Total= new double[nodes.getLength()];
		
		for(int i=0;i<titles.getLength();i++){
			Title[i]=titles.item(i).getTextContent();
			Total[i]=0;
			System.out.printf("Title[%d] : %s\n",i,Title[i]);
		}
		
		
		
		
		
		
		
		
		//index.post 읽어와서 계산
		FileInputStream fileStream= new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		System.out.println("읽어올 객체의 type: "+object.getClass());
		
		HashMap hashMap =(HashMap) object;
		Iterator<String> it=hashMap.keySet().iterator();
		
		
		int cnt=0;
		while(it.hasNext()) {
			String key=it.next();
			String value=(String)hashMap.get(key);
			//key랑 value는 정상 작동
//			System.out.println(cnt+key+value);
						
			for(int i=0; i<wordcnt;i++) {
				
//				System.out.println(cnt+key+value);
				if(key == word[i]) {
					//이게 왜 안되죠..					
					System.out.println(cnt);
				}else {
					System.out.println("응 if문 못들어가~");
				}
				
				
				
//				System.out.println(cnt+"   "+key+"    "+word[i]);
//				System.out.println(key+word[i]);
				
//				if(key == word[i])
////					System.out.println(key+word[i]+value);
//				
//				
//				if(key==word[i]) {
//					System.out.println("if문 들어왔다."+key+" "+word[i]);
//					
//					String[] temp=value.split(" ");
//					
//					System.out.println(cnt+"  "+word[i]+"  "+value);
//					
//					for(int j=0;j<temp.length;j=j+2) {
//						int id=Integer.parseInt(temp[j]);
//						
////						System.out.println("temp:"+temp[j+1]);
//						
//						Total[id]+=Integer.parseInt(temp[j+1])*Tf[i];
//						
//					}
//				}
			}
			
			cnt++;
		}
	
		
		System.out.println("문서별 유사도");
		for(int i=0; i<nodes.getLength();i++) {
			System.out.printf("%s : %.2f\n", Title[i], Total[i]);
		}

		
//		각 문서와 내적해서(쿼리 1번째 단어 빈도수 x i번째 다큐먼트에서 그 단어의 가중치    들의 합)
//		크기순 3개만 출력
//		
//		
//		검색되는 문서의 유사도가 0일 경우
//
//		"검색된 문서가 없습니다" 와 같은 문구를 출력하시면 됩니다
//
//		2. 검색되는 문서(가중치값이 0보다 큰 문서)가 3개보다 적은 경우
//
//		- 검색되는 문서만 출력하시면 됩니다
//
//		3. 검색되는 문서가 모두 유사도가 같고, 3개보다 많은 경우
//
//		- id값이 작은 순서대로 3개만 출력하시면 됩니다
//
//		 4. 이번 실습 과제는 유사도에 따라 상위 3개 문서의 제목 (+유사도)를 출력하는 것입니다.
//
//		문서의 제목을 가져오기 위해서는 index.post 파일만으로 부족하기 때문에 
//		그 이전 주차인 index.xml을 상대경로로 읽어서 추가하는 방법을 사용하셔도 됩니다. 
//		그 밖에 문서의 제목을 가져오기 위한 다양한 방법을 사용하셔도 됩니다. 
//		단, 상대경로로 파일을 여셔야 된다는 점은 주의하시기 바랍니다.
//		
	}


}
