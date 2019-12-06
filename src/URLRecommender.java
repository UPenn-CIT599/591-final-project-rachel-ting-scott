import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Makes use of NYTimes data set (mapping news article URLs to extracted keywords).
 * Searches over the keyword list extracted from the user-provided URL and matches
 * keywords to NYTimes set, suggests URLs with matching keywords.
 * @author Rachel Friend, Ting-Hsuan Lee, Scott Theer
 */
public class URLRecommender{
	
	private ArrayList<String> recommend;
	 
	public URLRecommender() {
		recommend = new ArrayList<String>();
	}
	
	/**
	 * Runs ReadCSV class to read relevant NYTimes data into
	 * ArrayList (keys are keywords and values are URLs)
	 * @return NYTimes keyword ArrayList
	 */
	public HashMap<String[], String> createMap() {
		ReadCSV r = new ReadCSV();
		HashMap<String[], String> hash;
		try {
			hash = r.read();
		} catch (IOException e) {
			e.printStackTrace();
			hash = null;
		}
		return hash;
	}
	
	/**
	 * Runs through keywords from user-provided URL to check if these keywords
	 * exist in the NYTimes set. If they do, the URL associated with keyword is added
	 * to recommendation list.
	 * @param hash NYTimes keyword ArrayList
	 * @param topWords keywords from URL
	 */
	public void recommendURL(HashMap<String[], String> hash, ArrayList<String> topWords) {
		ArrayList<String> recommend = new ArrayList<String>();
		for(String[] keys : hash.keySet()) {
			for(String word : topWords) {
				if(Arrays.asList(keys).contains(word)) {
					recommend.add(hash.get(keys));
				}
			}
		}
		if(recommend.size() == 0) {
			System.out.println("Sorry, no recommendations!");
		}
	}
	
	/**
	 * Getter for recommendation list
	 * @return recommendation list
	 */
	public ArrayList<String> getRecommendations() {
		return recommend;
	}
	
}
