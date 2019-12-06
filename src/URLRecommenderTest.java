import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class URLRecommenderTest extends URLRecommender {

	@Test
	void testRecommendURL() {
		URLRecommender url = new URLRecommender();
		HashMap<String[], String> hash = url.createMap();
		
		ArrayList<String> topWords = new ArrayList<String>();
		topWords.add("trump");
		topWords.add("war");
		topWords.add("lawmakers");
		topWords.add("china");
		topWords.add("impeachment");
		
		url.recommendURL(hash, topWords);
		ArrayList<String> recommendations = url.getRecommendations();
		
		assertEquals(recommendations.contains("https://www.nytimes.com/2017/10/11/opinion/china-asia-skyscrapers.html"), true, "The HashMap is missing recommendations!");
	}

}
