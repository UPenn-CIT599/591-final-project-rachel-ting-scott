import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

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
	private InputStream entityModelIn = null;

	WebScraper webscraper = new WebScraper();
	String out = webscraper.runScraper();
	private String userWords = out;

	//Just use the following String variable when not connected to WebScraper.java
	//	private String userWords = "The story goes like this. ADD MORE :)";	
	private ArrayList<String> stopWordsArrayList = new ArrayList<String>();
	//	private String[] tokensArray;
	private ArrayList<String> tokenArrayList;
	private ArrayList<String> lemmaArrayList;
	private HashMap<String, Integer> lemmaToCountMap;
	private List<Map.Entry<String, Long>> topLemmaToCountMap;
	private ArrayList<String> peopleInArticleArrayList;
	private List<Map.Entry<String, Long>> topPeopleToCountMap;

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
	public ArrayList<String> tokenizer(String str) {
		try {
			//reading OpenNLP tokens model to a stream
			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			String[] tokensArray = tokenizer.tokenize(str.toLowerCase());
			tokenArrayList = new ArrayList<String>();
			for(String token : tokensArray) {
				tokenArrayList.add(token);
			}

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

		//remove stop word from userWords using the stop-words-en.txt list of custom stop words
		try {
			File readStopWords = new File("stop-words-en.txt");
			Scanner sc = new Scanner(readStopWords);
			while (sc.hasNext()) {
				String stopWord = sc.next();
				stopWordsArrayList.add(stopWord);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Stop Word file is not found.");
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
		tokenArrayList.removeAll(stopWordsArrayList);
		System.out.println(tokenArrayList);
		return tokenArrayList;
	}

	/**
	 * Grabs all the tokens; counts their frequency; stores those key-value pairs in a HashMap
	 * @return lemmaToCountMap
	 */
	public HashMap<String, Integer> createTokenToCountMap() {	
		ArrayList<String> tempTokens = tokenizer(userWords);
		lemmaToCountMap = new HashMap<>();
		for (String str : tempTokens) {
			if (lemmaToCountMap.containsKey(str)) {
				int tempCount = lemmaToCountMap.get(str);
				tempCount++;
				lemmaToCountMap.put(str, tempCount);
			}
			else {
				lemmaToCountMap.put(str, 1);
			}
		}
		//		for (String s : stopWordsArrayList) {
		//			lemmaToCountMap.remove(s);
		//		}

		//TESTING
		System.out.println();
		System.out.println("lemmaToCountMap: " + lemmaToCountMap);
		return lemmaToCountMap;
	}

	/**
	 * Takes tokens ArrayList and tags each token for POS to then 
	 * return an arraylist of the lemmas of each word, excluding custom, pre-defined stop words.
	 * The lemmas are used for more accurate keyword tagging in the KeywordAnalysis class.
	 * @return arraylist of lemmas (not including custom, pre-defined stop words
	 */
	public ArrayList<String> lemmatizer() {
		ArrayList<String> tempTokensArrayList = tokenizer(userWords);
		//		ArrayList<String> temp = tokenizerForArrayList(userWords);

		try {
			// reading OpenNLP parts-of-speech model to a stream
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading OpenNLP parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// OpenNLP Tag method for tagging the tokens (which takes in array, thus the conversion from array list to array
			String[] tempTokensArray = new String[tempTokensArrayList.size()]; 
			tempTokensArray = tempTokensArrayList.toArray(tempTokensArray); 			
			String[] tags = posTagger.tag(tempTokensArray);

			// loading the dictionary to input stream
			dictLemmatizer = new FileInputStream("en-lemmatizer.dict");

			// loading the lemmatizer with dictionary
			DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

			// OpenNLP lemmatize method for putting the lemma in a String[]
			String[] lemmas = lemmatizer.lemmatize(tempTokensArray, tags);

			// converting lemmas String[] to arraylist
			lemmaArrayList = new ArrayList<String>();
			for(String lemma : lemmas) {
				if (!lemma.equals("O")) {
					lemmaArrayList.add(lemma);
				}
			}	      

			//			lemmaArrayList.removeAll(stopWordsArrayList);
			//TESTING: printing the results
			//			System.out.println("LEMMALIST: " + lemmaArrayList);
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
		finally {
			if (posModelIn != null) {
				try {
					posModelIn.close();
				}
				catch (IOException e) {
				}
			}
		}	
		return lemmaArrayList;

	}

	/**
	 * Creates HashMap of lemma:count and returns the top n lemma
	 * @return int top n lemma
	 */
	public List<Entry<String, Long>> findTopLemma() {
		//		HashMap<String, Integer> lemmaToCountMap = new HashMap<>();
		//		for (String str : lemmaArrayList) {
		//			if (lemmaToCountMap.containsKey(str)) {
		//				int tempCount = lemmaToCountMap.get(str);
		//				tempCount++;
		//				lemmaToCountMap.put(str, tempCount);
		//			}
		//			else {
		//				lemmaToCountMap.put(str, 1);
		//			}
		//		}

		Map<String, Long> tempMap = lemmaArrayList.stream()
				.collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		topLemmaToCountMap = tempMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				//limit is hard-coded for how many top words you want returned
				.limit(5)
				.collect(Collectors.toList());

		//TESTING
		System.out.println();
		//			System.out.println("lemmaToCountMap: " + lemmaToCountMap);
		System.out.println("topLemmaToCountMap: " + topLemmaToCountMap);
		return topLemmaToCountMap;
	}

	public ArrayList<String> findPeople() {
		peopleInArticleArrayList = new ArrayList<>();
		try {
			//must tokenize the userWords again because stop words cannot be removed for named entity recognition methods to properly function
			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			entityModelIn = new FileInputStream(new File("en-ner-person.bin"));
			TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(entityModelIn);
			NameFinderME nameFinderME = new NameFinderME(tokenNameFinderModel);

			String text = userWords;
			String tokens[] = tokenizer.tokenize(text);
			Span nameSpans[] = nameFinderME.find(tokens);

			for (int i = 0; i < nameSpans.length; i++) {
				String name = tokens[nameSpans[i].getStart()];
				peopleInArticleArrayList.add(name);
			}			

			String namePlusProbability = "";
			System.out.println("\n------Name : Probability of Accuracy------");
			for (int i = 0; i < peopleInArticleArrayList.size(); i++) {
				for (int j = 0; j < nameSpans.length; j++) {
					namePlusProbability = peopleInArticleArrayList.get(i) + " : " + nameSpans[i].getProb();
				}
				System.out.println(namePlusProbability);
			}
			//			for (String str : peopleInArticleArrayList) {
			//				System.out.println(str);
			//			}

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		finally {
			if (tokenModelIn != null) {
				try {
					tokenModelIn.close();
				}
				catch (IOException e) {
				}
			} if (entityModelIn != null) {
				try {
					entityModelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return peopleInArticleArrayList;
	}

	public List<Entry<String, Long>> findTopPeople() {
//		HashMap<String, Integer> topPeopleToCountMap = new HashMap<>();
//		for (String key : peopleInArticleArrayList) {
//			if (topPeopleToCountMap.containsKey(key)) {		
//				int tempCount = topPeopleToCountMap.get(key);
//				tempCount++;
//				topPeopleToCountMap.put(key, tempCount);
//			}
//			else {
//				topPeopleToCountMap.put(key, 1);
//			}
//		}
//		System.out.println("TOP PEOPLE MAP: " + topPeopleToCountMap);
		
		Map<String, Long> tempMap = peopleInArticleArrayList.stream()
				.collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		topPeopleToCountMap = tempMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				//limit is hard-coded for how many top words you want returned
				.limit(3)
				.collect(Collectors.toList());

		//TESTING
		System.out.println();
		System.out.println("topPeopleToCountMap: " + topPeopleToCountMap);

		return topPeopleToCountMap;

	}

	/**
	 * Getter methods for the following instance variables: userWords, lemmaArrayList, lemmaToCountMap
	 * @return
	 */
	public String getUserWords() {
		return userWords;
	}

	public ArrayList<String> getLemmaArrayList() {
		return lemmaArrayList;
	}

	public HashMap<String, Integer> getTokenToCountMap() {
		return lemmaToCountMap;
	}

	public static void main(String args[]) {
		NLPData nlp = new NLPData();
		//		System.out.println("Words from URL: " + nlp.getUserWords());
		//		nlp.tokenizer(nlp.getUserWords());
		nlp.createTokenToCountMap();
		nlp.lemmatizer();
		nlp.findTopLemma();
		nlp.findPeople();
		nlp.findTopPeople();

		//		System.out.println("TOKENSlIST: "); //TESTING
		//		System.out.println(nlp.tokenizerForArrayList(nlp.getUserWords()));

		//		System.out.println("userWords: " + nlp.getUserWords());
		//		nlp.createTokenToCountMap();
		//		nlp.tokenizerForArrayList(nlp.getUserWords());
	}

}
