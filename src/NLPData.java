import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class NLPData {
	/**
	 * This class uses Apache OpenNLP to take in a String, tokenize it (split it into individual words, 
	 * separate punctuation), and then label each token with their part of speech POS (see README for POS key)
	 * and the probability of their accuracy (the validity of the model).
	 * Tutorials from www.tutorialkart.com for basic use of OpenNLP methods and models
	 */
	private InputStream tokenModelIn = null;
	private InputStream posModelIn = null;
//	private InputStream dictLemmatizer = null;

	WebScraper webscraper = new WebScraper();
	String out = webscraper.runScraper();
	private String userWords = out;
	//Just use the following String variable when not connected to WebScraper.java
//	private String userWords = "The story goes like this. John is a 26 year-old. He is older than me, but I am smarter than him. "
//			+ "sad sadly sadder saddest slow slowly slower slowest not happy the news my car there are no cars. "
//			+ "she doesn't want to because she does not want to; however, she should. Whose car is that? "
//			+ "I want the teacher who is nice.";
	//customized list of stop words
	private String[] stopWords = {"the", "then", "than", "and", "an", "a", "or", "with", ",", ".", ";", "!", "save"};
	private ArrayList<String> stopWordsArrayList = new ArrayList<String>(Arrays.asList("the", 
			"then", "than", "and", "an", "a", "or", "with", ",", ".", ";", "!", "save"));	
	private String[] tokensArray;
	private ArrayList<String> tokensArrayList;
	private String[] tagsArray;
//	private String[] lemmaArray;
	private HashMap<String, Integer> tokenToCountMap;
	private HashMap<String, String> tokenToPOSTagMap;

	/**
	 * Constructor takes in a String array which is the cleaned text output from the webpage scraping
	 * of the user's URL input.
	 * @param userWords
	 */
	public NLPData(String userWords) {
		this.userWords = userWords;
	}

	/**
	 * Overloaded constructor for testing
	 */
	public NLPData() {

	}

	/**
	 * Helper method that takes the String of user's text and tokenizes them
	 * @return String[] of the tokens of the user's text
	 */
	public String[] tokenizer(String str) {
		try {
			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);

			tokensArray = tokenizer.tokenize(str.toLowerCase());
			//use when not connected to WebScraper.java
//			tokensArray = tokenizer.tokenize(userWords.toLowerCase());
//			//TESTING
//			System.out.println("tokensArray:");
//			for (String element : tokensArray) {
//				System.out.print(element + "\t");
//			}
//			System.out.println(); //TESTING
//			System.out.println(tokensArray[3]); //TESTING

		} catch (IOException e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		}
		finally {
			if (tokenModelIn != null) {
				try {
					tokenModelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return tokensArray;
	}
	
	/**
	 * this is the tokenizer with an output of an ArrayList instead of an array - I don't know which is better?
	 * @param str
	 * @return
	 */
	public ArrayList<String> tokenizerForArrayList(String str) {
		try {
			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			String[] temp = tokenizer.tokenize(str.toLowerCase());
			tokensArrayList = new ArrayList<>();
			for (String element : temp) {
				tokensArrayList.add(element);
			}
			tokensArrayList.removeAll(stopWordsArrayList);
			//I don't know if the above version or this version is better?
//			for (String stop : stopWordsArrayList) {
//				tokensArrayList.remove(stop);
//			}
			
//			//TESTING
//			System.out.println("tokensArray:");
//			for (String element : tokensArray) {
//				System.out.print(element + "\t");
//			}
//			System.out.println(); //TESTING
//			System.out.println(tokensArray[3]); //TESTING

		} catch (IOException e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		}
		finally {
			if (tokenModelIn != null) {
				try {
					tokenModelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
//		System.out.println("Tokens ArrayList: " + tokensArrayList);
		return tokensArrayList;
	}
	
	/**
	 * Grabs all the tokens; counts their frequency; stores those key-value pairs in a HashMap
	 * @return tokenToCountMap
	 */
	public HashMap<String, Integer> createTokenToCountMap() {	
		String[] tempTokens = tokenizer(userWords);
		tokenToCountMap = new HashMap<>();
		for (String str : tempTokens) {
			if (tokenToCountMap.containsKey(str)) {
				int tempCount = tokenToCountMap.get(str);
				tempCount++;
				tokenToCountMap.put(str, tempCount);
			}
			else {
				tokenToCountMap.put(str, 1);
			}
		}
		for (String str : stopWords) {
			tokenToCountMap.remove(str);
		}
		
		//TESTING
		System.out.println();
		System.out.println("tokenToCountMap: " + tokenToCountMap);
		return tokenToCountMap;
	}
	
	public String[] tagger() {
		String[] tempTokens = tokenizer(userWords);

		// reading parts-of-speech model to a stream
		try {
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			tagsArray = posTagger.tag(tempTokens);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TESTING
		System.out.println();
		System.out.print("Tags from .tagger: ");
		for (String str : tagsArray) {
			System.out.print(str +  "\t");
		}
		System.out.println();
		System.out.println("tempTokens: ");
		for (String str : tempTokens) {
			System.out.print(str +  "\t");
		}
		
		return tagsArray;

	}

	/**
	 * Getter methods for the following instance variables: userWords, stopWords, tokenToCountMap, and tokenToPOSTagMap
	 * @return
	 */
	public String getUserWords() {
		return userWords;
	}

	public String[] getStopWords() { //DELETE THIS ONE? NOT SURE WHY THIS WOULD NEED TO BE SHARED???
		return stopWords;
	}

	public HashMap<String, Integer> getTokenToCountMap() {
		return tokenToCountMap;
	}

	public HashMap<String, String> getTokenToPOSTagMap() {
		return tokenToPOSTagMap;
	}

	public static void main(String args[]) {
//		WebScraper webscraper = new WebScraper();
//		String out = webscraper.runScraper();
		NLPData nlp = new NLPData();
		System.out.println("Words from URL: " + nlp.getUserWords());
		nlp.createTokenToCountMap();
		nlp.tagger();
		System.out.println("Tokens as arraylist: ");
		System.out.println(nlp.tokenizerForArrayList(nlp.getUserWords()));
//		System.out.println("userWords: " + nlp.getUserWords());
//		nlp.createTokenToCountMap();
//		nlp.tagger();
//		nlp.tokenizerForArrayList(nlp.getUserWords());
	}

}
