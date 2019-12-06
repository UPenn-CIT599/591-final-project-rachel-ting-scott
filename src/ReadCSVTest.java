import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

class ReadCSVTest extends ReadCSV {

	@Test
	void testReadSize() {
		URLRecommender url = new URLRecommender();
		HashMap<String[], String> hash = url.createMap();
		assertEquals(hash.size(), 284, "The HashMap should have size 284!");
	}
	
	void testReadContent() {
		URLRecommender url = new URLRecommender();
		HashMap<String[], String> hash = url.createMap();
		boolean test = false;
		for(String[] keys : hash.keySet()) {
			List<String> keyList = Arrays.asList(keys);
			if(keyList.contains("Dove Drops an Ad Accused of Racism")) {
				test = true;
			}
		}
		assertEquals(test, false, "The HashMap contains invalid keys!");
	}

}
