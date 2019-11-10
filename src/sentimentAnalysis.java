import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class sentimentAnalysis {
	private HashMap<String, String> sentimentDict;
	private int negativeWordCount;
	private int positiveWordCount;
	private int scoreOutput;
		
	public sentimentAnalysis() {
		sentimentDict = new HashMap<String, String>();
		negativeWordCount = 0;
		positiveWordCount = 0;
		scoreOutput = 0;
	}
		
	/**
	 * This method creates a dictionary of positive and negative words
	 */
	public void createDictionary() {
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
	
	
	/**
	 * This method counts the positive and negative words in the text
	 * @param NLPAnalysisResult HashMap
	 */
	public void wordCounter(HashMap<String, Integer> NLPAnalysisResult) {
		for (String key : NLPAnalysisResult.keySet()) {
			String sentimentResult = sentimentDict.get(key.toLowerCase());
			if (sentimentResult.equals("n")) {
				negativeWordCount += NLPAnalysisResult.get(key);
			}
			else if (sentimentResult.equals("p")) {
				positiveWordCount += NLPAnalysisResult.get(key);
			}
		}
	}
	
	/**
	 * This method calculates the positivity score by assigning the negative words with -1
	 * and the positive words with 1.
	 * @return scoreOutput
	 */
	public int scoreCounter() {		
		scoreOutput = negativeWordCount * (-1) + positiveWordCount;				
		return scoreOutput;
	}
	
	
	public HashMap<String, String> getSentimentDict() {
		return sentimentDict;
	}


	public int getNegativeWordCount() {
		return negativeWordCount;
	}


	public int getPositiveWordCount() {
		return positiveWordCount;
	}


	public int getScoreOutput() {
		return scoreOutput;
	}
	

//	public static void main(String[] args) {
//		sentimentAnalysis sA = new sentimentAnalysis();
//		for(String key : sA.getSentimentDict().keySet()) {
//			System.out.println(key + "," + sA.getSentimentDict().get(key));
//		}
//	}

}
