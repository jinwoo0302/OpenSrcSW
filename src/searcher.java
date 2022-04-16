import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
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
	public static void InnerProduct(String path, String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		
		//입력받은 query를 형태소 분석
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
		
		//body태그로 나눠서 노드에 넣기
		NodeList nodes = document.getElementsByTagName("body");
//		System.out.println("파싱할 리스트 수 : "+nodes.getLength());
		
	
		
		//문서와의 유사도 초기화
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
		
		
		
		//코사인 유사도로 변경하기 위해 query와 문서의 크기 계산
		double qsize=0;
		double[] idsize=new double[nodes.getLength()];
		Arrays.fill(idsize, 0);		
		for(int i=0; i<wordcnt;i++) {
			qsize+=Math.pow(Tf[i],2);
			
		}
		qsize=Math.sqrt(qsize);
		
		
		
		
		
		
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
						idsize[id]+=Math.pow(Double.parseDouble(temp[j+1]), 2);
					}
				}
			}
			
			cnt++;
		}
		
		
		
		for(int i=0;i<idsize.length;i++) {
			idsize[i]=Math.sqrt(idsize[i]);
		}
		
		
		
		for(int i=0; i<Title.length; i++){
			if(idsize[i]>0) {
				Total[i]=Total[i]/(qsize*idsize[i]);
//			System.out.printf("%s: %.2f\n",Title[i],Total[i]);
			}
		}
		
		
		
		
		
//		System.out.println("\n내림차순 정렬 후:");
		int[] cmp=new int[Title.length];
		for(int i=0; i<Title.length; i++){
			cmp[i]=i;
		}
		
		
		for(int i=0; i<Title.length-1; i++){
            for(int j=i+1; j<Title.length; j++){
                if(Total[cmp[i]]<Total[cmp[j]]){   
                    int tmp=cmp[i];
                    cmp[i]=cmp[j];
                    cmp[j]=tmp;
                }
            }
        }
		
//		for(int i=0; i<Title.length; i++){
//			System.out.println(cmp[i]);
//		}
//		
		
        for(int i=0;i<3;i++){
        	if(Total[cmp[0]]==0) {
        		System.out.println("검색된 문서가 없습니다.");
        		break;
        	}
        	if(Total[cmp[i]]!=0) {
        		System.out.printf("%s: %.2f\n",Title[cmp[i]],Total[cmp[i]]);
        	}
            
        }
	}


}
