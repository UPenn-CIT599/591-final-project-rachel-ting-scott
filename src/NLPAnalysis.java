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
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * This class uses Apache OpenNLP to take in a String, tokenize it (split it into individual words, 
 * separate punctuation), and then label each token with their part of speech POS (see README for POS key)
 * and the probability of their accuracy (the validity of the model).
 * Tutorials from www.tutorialkart.com for basic use of OpenNLP methods and models
 */
public class NLPAnalysis {
	//	private String[] userWordsArray;
	private String userWords = "The story goes like this. John is a 26 year-old. He is older than me, but I am smarter than him. "
			+ "sad sadly sadder saddest slow slowly slower slowest not happy the news my car there are no cars. "
			+ "she doesn't want to because she does not want to; however, she should. Whose car is that? "
			+ "I want the teacher who is nice.";
	private String userWordsMinusStopWords;
	private InputStream tokenModelIn = null;
	private InputStream posModelIn = null;
	private InputStream dictLemmatizer = null;

	//customized list of stop words
	private String[] stopWords = {"the", "and", "an", "a", "or", "with"};
	private String[] tokensArray;
	private String[] tagsArray;
	private String[] lemmaArray;
	private HashMap<String, Integer> tokenToCountMap;
	private HashMap<String, String> tokenToPOSTagMap;

	/**
	 * Constructor takes in a String array which is the cleaned text output from the webpage scraping
	 * of the user's URL input.
	 * @param userWordsArray
	 */
	public NLPAnalysis(String userWords) {
		this.userWords = userWords;
	}
	/**
	 * Overloaded constructor
	 */
	public NLPAnalysis() {
		
	}

	/**
	 * Remove stop words from the user's URL text. 
	 * @param userWords is the text input
	 * @param stopWords are the words that are custom defined as stop words 
	 */
	public String removeStopWords (String userWords, String[] stopWords) {
		System.out.println("Original text: " + userWords);
		userWords = userWords.toLowerCase().trim();

		ArrayList<String> wordList = new ArrayList<>();
		wordList.addAll(Arrays.asList(userWords.split(" ")));

		List<String> stopWordList = new ArrayList<>();
		stopWordList.addAll(Arrays.asList(stopWords));

		wordList.removeAll(stopWordList);
		System.out.println("Text without stop words: " + wordList.toString());
		return userWordsMinusStopWords;
	}

	/**
	 * Helper method that takes the String of user's text and tokenizes them
	 * @return String[] of the tokens of the user's text
	 */
	public String[] tokenizer() {
		try {
			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);

			tokensArray = tokenizer.tokenize(userWords.toLowerCase());
			//TESTING
			//			System.out.println("tokensArray:");
			//			for (String str : tokensArray) {
			//				System.out.print(str + "\t");
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
	 * Grabs all the tokens; counts their frequency; stores those key-value pairs in a HashMap
	 * @return tokenToCountMap
	 */
	public HashMap<String, Integer> createTokenToCountMap() {
		String[] temp = tokenizer();
		tokenToCountMap = new HashMap<>();
		for (String str : temp) {
			if (tokenToCountMap.containsKey(str)) {
				int tempCount = tokenToCountMap.get(str);
				tempCount++;
				tokenToCountMap.put(str, tempCount);
			}
			else {
				tokenToCountMap.put(str, 1);
			}
		}
		System.out.println(tokenToCountMap);
		return tokenToCountMap;
	}

	public String[] tagger() {
		String[] temp = tokenizer();
//		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE; //TESTING
//		String[] tokens = tokenizer.tokenize("John has a sister named Penny."); //TESTING
		// reading parts-of-speech model to a stream
		try {
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			tagsArray = posTagger.tag(temp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		
//		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
//		String[] tokens = tokenizer.tokenize("John has a sister named Penny.");
//
//		InputStream inputStreamPOSTagger = getClass()
//				.getResourceAsStream("en-pos-maxent.bin");
//		POSModel posModel;
//		try {
//			posModel = new POSModel(inputStreamPOSTagger);
//			POSTaggerME posTagger = new POSTaggerME(posModel);
//			tagsArray = posTagger.tag(tokens);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//TESTING
		System.out.print("Tags from .tagger: ");
		for (String str : tagsArray) {
			System.out.print(str +  "\t");
		}
		return tagsArray;

	}
	/**
	 * Tags the parts of speech of each word in the user's input and stores those key-value pairs in a hashmap
	 * @return HashMap of token to POS
	 */
	public HashMap<String, String> createTokenToPOSTagMap() {
		String[] temp = tokenizer();
		try {
			// Parts-Of-Speech Tagging
			// reading parts-of-speech model to a stream
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			tagsArray = posTagger.tag(temp);
			//			String tags[] = posTagger.tag(tokensArray); //look up tags as datatype
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
		//TESTING
		System.out.println("Token\t:\tTag");
		for(int i = 0; i < temp.length; i++){
			System.out.println(temp[i] + "\t:\t" + tagsArray[i]);
		}

		for(int i = 0; i < temp.length; i++){
			for (int j = 0; j < tagsArray.length; j++) {
				if (!tokenToPOSTagMap.containsKey(temp[i])) {
					tokenToPOSTagMap.put(temp[i], tagsArray[j]);
				}
			}
		}
		System.out.println(tokenToPOSTagMap);
		return tokenToPOSTagMap;
	}

	/**
	 * Lematizes the words from the user's URL (minus stop words)
	 * -----this might need to move to the KeywordAnalysis class-----
	 * @return lemmaArray
	 */
	public String[] nlpLemmatize() {
		return lemmaArray;		
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
//		NLPAnalysis nlp = new NLPAnalysis("The story goes like this. John is a 26 year-old. He is older than me, but I am smarter than him. "
//				+ "sad sadly sadder saddest slow slowly slower slowest not happy the news my car there are no cars. "
//				+ "she doesn't want to because she does not want to; however, she should. Whose car is that? "
//				+ "I want the teacher who is nice.");
NLPAnalysis nlp = new NLPAnalysis();
		System.out.println("userWords: " + nlp.userWords);
		nlp.createTokenToCountMap();

		//		removeStopWords(sentence, stopWords);
		nlp.removeStopWords(nlp.userWords, nlp.stopWords);
		//		nlp.removeStopWords(nlp.userWords, stopWords);

		nlp.tagger();

		//		nlp.createTokenToPOSTagMap();
	}
}