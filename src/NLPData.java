import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
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
	private InputStream dictLemmatizer = null;

	WebScraper webscraper = new WebScraper();
	String out = webscraper.runScraper();
	private String userWords = out;

	//Just use the following String variable when not connected to WebScraper.java
	//	private String userWords = "The story goes like this. ADD MORE :)";
	//customized list of stop words --> need to convert to a .txt file that gets read in
	private ArrayList<String> stopWordsArrayList = new ArrayList<String>(Arrays.asList("the", 
			"then", "than", "and", "an", "a", "or", "with", ",", ".", ";", "!", "java"));	
	private String[] tokensArray;
	//	private ArrayList<String> tokensArrayList; //DELETE??
	private ArrayList<String> lemmaArrayList;
	private HashMap<String, Integer> tokenToCountMap;

	/**
	 * Constructor takes in a String array which is the cleaned text output from the webpage scraping
	 * of the user's URL input.
	 * @param userWords
	 */
	public NLPData(String userWords) {
		this.userWords = userWords;
	}

	/**
	 * Overloaded constructor
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

	////DELETE??
	//	/**
	//	 * this is the tokenizer with an output of an ArrayList instead of an array - 
	//	 * I don't know which is better? but I think the arraylist is better because it can remove stopwords before
	//	 * other analysis.
	//	 * @param str
	//	 * @return
	//	 */
	//	public ArrayList<String> tokenizerForArrayList(String str) {
	//		try {
	//			tokenModelIn = new FileInputStream("en-token.bin");
	//			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
	//			Tokenizer tokenizer = new TokenizerME(tokenModel);
	//			String[] temp = tokenizer.tokenize(str.toLowerCase());
	//			tokensArrayList = new ArrayList<>();
	//			for (String element : temp) {
	//				tokensArrayList.add(element);
	//			}
	//			tokensArrayList.removeAll(stopWordsArrayList);
	//			//I don't know if the above version or this version is better?
	//			//			for (String stop : stopWordsArrayList) {
	//			//				tokensArrayList.remove(stop);
	//			//			}
	//
	//			//			//TESTING
	//			//			System.out.println("tokensArray:");
	//			//			for (String element : tokensArray) {
	//			//				System.out.print(element + "\t");
	//			//			}
	//			//			System.out.println(); //TESTING
	//			//			System.out.println(tokensArray[3]); //TESTING
	//
	//		} catch (IOException e) {
	//			// Model loading failed, handle the error
	//			e.printStackTrace();
	//		}
	//		finally {
	//			if (tokenModelIn != null) {
	//				try {
	//					tokenModelIn.close();
	//				}
	//				catch (IOException e) {
	//				}
	//			}
	//		}
	//		//		System.out.println("Tokens ArrayList: " + tokensArrayList);
	//		return tokensArrayList;
	//	}

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
		for (String s : stopWordsArrayList) {
			tokenToCountMap.remove(s);
		}

		//TESTING
		System.out.println();
		System.out.println("tokenToCountMap: " + tokenToCountMap);
		return tokenToCountMap;
	}

	/**
	 * Takes tokens array (***change to tokens ArrayList?***??) and tags each token for POS to then 
	 * return an arraylist of the lemmas of each word, excluding custom, pre-defined stop words.
	 * The lemmas are used for more accurate keyword tagging in the KeywordAnalysis class.
	 * @return arraylist of lemmas (not including custom, pre-defined stop words
	 */
	public ArrayList<String> lemmatizer() {
		String[] tempTokens = tokenizer(userWords);
		//		ArrayList<String> temp = tokenizerForArrayList(userWords);

		try {
			// Parts-Of-Speech Tagging
			// reading parts-of-speech model to a stream
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			String[] tags = posTagger.tag(tempTokens);

			// loading the dictionary to input stream
			//InputStream dictLemmatizer = new FileInputStream("dictionary"+File.separator+"en-lemmatizer.txt");
			dictLemmatizer = new FileInputStream("en-lemmatizer.dict");

			// loading the lemmatizer with dictionary
			DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

			// finding the lemmas
			String[] lemmas = lemmatizer.lemmatize(tempTokens, tags);

			// converting lemmas list to arraylist
			lemmaArrayList = new ArrayList<String>();
			for(String lemma : lemmas) {
				lemmaArrayList.add(lemma);
			}	      

			lemmaArrayList.removeAll(stopWordsArrayList);
			System.out.println("LEMMALIST: " + lemmaArrayList);
			//TESTING: printing the results
			//			System.out.println("\nPrinting lemmas for the given sentence...");
			//			System.out.println("WORD -POSTAG : LEMMA");
			//			for(int i = 0; i < tempTokens.length; i++){
			//				System.out.println(tempTokens[i]+" -"+tags[i]+" : "+lemmas[i]);
			//			}
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lemmaArrayList;

	}

	/**
	 * Getter methods for the following instance variables: userWords, lemmaArrayList, tokenToCountMap
	 * @return
	 */
	public String getUserWords() {
		return userWords;
	}

	public ArrayList<String> getLemmaArrayList() {
		return lemmaArrayList;
	}

	public HashMap<String, Integer> getTokenToCountMap() {
		return tokenToCountMap;
	}

	public static void main(String args[]) {
		NLPData nlp = new NLPData();
//		System.out.println("Words from URL: " + nlp.getUserWords());
		nlp.createTokenToCountMap();
		nlp.lemmatizer();
		
//		System.out.println("TOKENSlIST: ");
		//		System.out.println(nlp.tokenizerForArrayList(nlp.getUserWords()));

		//		System.out.println("userWords: " + nlp.getUserWords());
		//		nlp.createTokenToCountMap();
		//		nlp.tokenizerForArrayList(nlp.getUserWords());
	}

}
