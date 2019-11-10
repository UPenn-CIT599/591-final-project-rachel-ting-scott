import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * This class modified tutorial code from www.tutorialkart.com for the specific purposes of this project
 * This class uses Apache OpenNLP to take in a String, tokenize it (split it into individual words, 
 * separate punctuation), and then label each token with their part of speech POS (see README for POS key)
 * and the probability of their accuracy (the validity of the model).
 */
public class NLPAnalysis {
	//	private String[] userWordsArray;
	private String userWords; //a String or a String[] ??
	private InputStream tokenModelIn = null;
	private InputStream posModelIn = null;
	private InputStream dictLemmatizer = null; //(?)

	private String[] tokensArray;
	private HashMap<String, String> tokenToPOSTagMap;
	private HashMap<String, Integer> tokenToCountMap;

	/**
	 * Constructor takes in a String array which is the cleaned text output from the webpage scraping
	 * of the user's URL input.
	 * @param userWordsArray
	 */
	public NLPAnalysis(String userWords) {
		this.userWords = userWords;
	}

	/**
	 * Tags the parts of speech of each word in the user's input and stores those key-value pairs in a hashmap
	 * @return HashMap of token to POS
	 */
	public HashMap<String, String> createTokenToPOSTagMap() {

		try {
			// tokenize the sentence
			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			tokensArray = tokenizer.tokenize(userWords);
			//TESTING 
			for (String str : tokensArray) {
				System.out.println(str);
			}

			// Parts-Of-Speech Tagging
			// reading parts-of-speech model to a stream
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
//			String[] tagsArray = posTagger.tag(tokensArray);
			String tags[] = posTagger.tag(tokensArray); //look up tags as datatype
			//TESTING
			for (String str : tags) {
				System.out.println(str);
			}
			
			//TESTING
			System.out.println("Token\t:\tTag");
			for(int i = 0; i < tokensArray.length; i++){
				System.out.println(tokensArray[i] + "\t:\t" + tags[i]);
			}

			for(int i = 0; i < tokensArray.length; i++){
				for (int j = 0; j < tags.length; j++) {
					if (!tokenToPOSTagMap.containsKey(tokensArray[i])) {
						tokenToPOSTagMap.put(tokensArray[i], tags[j]);
					}
					System.out.println(tokenToPOSTagMap);
//					else {
//						tokenToPOSTagMap.put(tokensArray[i], tagsArray[j]);
//					}
				}
			}
		}
		catch (IOException e) {
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
			if (posModelIn != null) {
				try {
					posModelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		System.out.println(tokenToPOSTagMap);
		return tokenToPOSTagMap;
	}

	/**
	 * Grabs all the adjectives from the tokens; counts their frequency; stores those key-value pairs in a HashMap
	 * @return tokenToCountMap
	 */
	public HashMap<String, Integer> createTokenToCountMap() {
		return tokenToCountMap;
	}

	/**
	 * Getter for tokenToCountMap to be used by SentimentAnalysis
	 * @return tokenToCountMap
	 */
	public HashMap<String, Integer> getTokenToCountMap() {
		return tokenToCountMap;
	}
	
	/**
	 * getter for the tokensArray
	 * @return
	 */
	public String[] getTokensArray() {
		return tokensArray;
	}

	//main for TESTING
	public static void main(String[] args) {
		NLPAnalysis nlp = new NLPAnalysis("John is 26 years old. He is older than me, but I am smarter than him. "
				+ "sad sadly sadder saddest slow slowly slower slowest not happy the news my car there are no cars. "
				+ "she doesn't want to because she does not want to; however, she should. Whose car is that? "
				+ "I want the teacher who is nice.");

		nlp.createTokenToPOSTagMap();
		nlp.createTokenToCountMap();
	}
}