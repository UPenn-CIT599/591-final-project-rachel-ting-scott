import java.io.InputStream;
import java.util.ArrayList;

public class KeywordAnalysis {	
	private InputStream keywordModelIn = null;
	private String[] keywordArray;
	//or
	private ArrayList<String> keywordArrayList;
	
	/**
	 * Use our trained model to find keywords and thus identify the topic(s) of the text
	 * The dataset "nytimes.csv" from Kaggle.com was used to train the keywordAnalysis.bin model
	 * @return a array list of strings which are the top 3-4 keywords
	 */
	public ArrayList<String> nlpKeywordTagger() {
		NLPData nlpData = new NLPData();
		ArrayList<String> keywordsTEMP = new ArrayList<>();
		keywordsTEMP = nlpData.getLemmaArrayList();
		System.out.println(keywordsTEMP);
		return keywordsTEMP;
	}

	public static void main(String args[]) {
		KeywordAnalysis keywords = new KeywordAnalysis();
		keywords.nlpKeywordTagger();
	}
 }
