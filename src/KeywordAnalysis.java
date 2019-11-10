import java.io.InputStream;

public class KeywordAnalysis {	
	private String userWords; //a String or a String[] ??
	
	private InputStream keywordModelIn = null;
	private String[] keywordArray;
	
	public KeywordAnalysis(String userWords) {
		this.userWords = userWords;
	}
	
	/**
	 * Use our trained model to find keywords and thus identify the topic(s) of the text
	 * @return a string array of the top 3-4 keywords
	 */
	public String[] nlpKeywordTag() {
		return keywordArray;
	}

}
