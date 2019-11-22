import java.io.IOException;
import java.util.List;

public class KeywordRunner extends KeywordsExtractor {

	public static void main(String[] args) {
		int num = 10;
		WebScraper webscraper = new WebScraper();
		String out = webscraper.runScraper();
    	KeywordsExtractor kw = new KeywordsExtractor();
    	try {
    		List<CardKeyword> keywordsList = kw.getKeywordsList(out);
    		for(int i=0; i < num; i++) {
    			CardKeyword keyword = keywordsList.get(i);
    			String stem = keyword.getStem();
    			if(stem.length() >  2) {
    				System.out.println(i+1 + ".");
    				System.out.println("stem: " + stem);
        			System.out.println("terms: " +keyword.getTerms());
        			System.out.println("frequency: " + keyword.getFrequency());
    			}	
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}
