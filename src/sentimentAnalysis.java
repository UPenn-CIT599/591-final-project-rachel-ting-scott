import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class sentimentAnalysis {
	private HashMap<String, String> sentimentDict;
		
	public sentimentAnalysis() {
		sentimentDict = new HashMap<String, String>();
		File negative = new File("negative-words.txt");
		try {
			Scanner in = new Scanner(negative);
			while(in.hasNextLine()) {
				String word = in.nextLine();
				sentimentDict.put(word, "n");
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File positive = new File("positive-words.txt");
		try {
			Scanner in2 = new Scanner(positive);
			while(in2.hasNextLine()) {
				String word = in2.nextLine();
				sentimentDict.put(word, "p");
			}
			in2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public HashMap<String, String> getSentimentDict() {
		return sentimentDict;
	}


//	public static void main(String[] args) {
//		sentimentAnalysis sA = new sentimentAnalysis();
//		for(String key : sA.getSentimentDict().keySet()) {
//			System.out.println(key + "," + sA.getSentimentDict().get(key));
//		}
//	}

}
