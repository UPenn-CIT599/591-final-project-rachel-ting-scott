import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class sentimentAnalysis {
	private HashMap<String, String> sentimentDict;
	private int negativeWordCount;
	private int positiveWordCount;
	private int scoreOutput;
		
	public sentimentAnalysis(HashMap<String, Integer> NLPResult) {
		sentimentDict = new HashMap<String, String>();
		negativeWordCount = 0;
		positiveWordCount = 0;
		scoreOutput = 0;
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
		
		wordCounter(NLPResult);
		scoreCounter();
		System.out.println(scoreOutput);
		
	}

	
	/**
	 * This method counts the positive and negative words in the text
	 * @param NLPAnalysisResult HashMap
	 */
	public void wordCounter(HashMap<String, Integer> NLPAnalysisResult) {
		for (String key : NLPAnalysisResult.keySet()) {
			String sentimentResult = sentimentDict.get(key.toLowerCase());
			if (sentimentResult != null) {
				if (sentimentResult.equals("n")) {
					negativeWordCount = negativeWordCount + NLPAnalysisResult.get(key);
				}
				else if (sentimentResult.equals("p")) {
					positiveWordCount = positiveWordCount + NLPAnalysisResult.get(key);
				}
			}			
//			else {
//				;
//			}	
		}
	}
	
	/**
	 * This method calculates the positivity score by assigning the negative words with -1
	 * and the positive words with 1.
	 * @return scoreOutput
	 */
	public int scoreCounter() {		
		scoreOutput = positiveWordCount - negativeWordCount;				
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
	

	public static void main(String[] args) {
		
		HashMap<String, Integer> trial = new HashMap<String, Integer>();
		trial.put("Sherry", 3);
		trial.put("abound", 1);
		trial.put("abominable", 3);
		trial.put("apple", 2);
		trial.put("adaptable", 7);
		trial.put("Accurate", 10);
		
		sentimentAnalysis sA = new sentimentAnalysis(trial);
//		for(String key : sA.getSentimentDict().keySet()) {
//			System.out.println(key + "," + sA.getSentimentDict().get(key));
//		}
		
		
	}

}
