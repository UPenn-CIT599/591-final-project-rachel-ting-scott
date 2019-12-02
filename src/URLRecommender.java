import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class URLRecommender{
	
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
	
	public ArrayList<String> recommendURL(HashMap<String[], String> hash, ArrayList<String> topWords) {
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
		return recommend;
	}
	
	public static void main(String[] args) {
		URLRecommender url = new URLRecommender();
		HashMap<String[], String> hash = url.createMap();
		
		ArrayList<String> topWords = new ArrayList<String>();
		topWords.add("trump");
		topWords.add("war");
		topWords.add("lawmakers");
		topWords.add("china");
		topWords.add("impeachment");
		
		ArrayList<String> recommendations = url.recommendURL(hash, topWords);
		for(String s : recommendations) {
			System.out.println(s);
		}
	}
}
