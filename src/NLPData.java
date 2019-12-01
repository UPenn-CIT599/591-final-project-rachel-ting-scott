import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

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
	 * This class uses Apache OpenNLP to take in a String of text as input from the WebScraper class, tokenize it, remove stop words, 
	 * count the frequency of each token (for the purpose of the sentiment analysis), lemmatize the tokens, 
	 * store those in a hashmap and pull out the top n content words for output,
	 * extract the names of people and store those in a hashmap and pull out the top n people mentioned for output
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
	private ArrayList<String> tokenArrayList;
	private ArrayList<String> lemmaArrayList;
	private HashMap<String, Integer> tokenToCountMap;
	private List<Map.Entry<String, Long>> topLemmaToCountList;
	private ArrayList<String> peopleInArticleArrayList;
	private List<Map.Entry<String, Long>> topPeopleToCountList;

	//to be used in potential future iterations
//	/**
//	 * Constructor takes in a String array which is the cleaned text output from the webpage scraping
//	 * of the user's URL input.
//	 * @param userWords
//	 */
//	public NLPData(String userWords) {
//		this.userWords = userWords;
//
//	}

	/**
	 * Constructor
	 */
	public NLPData() {

	}

	/**
	 * Helper method that takes the String of user's text and tokenizes it and removes the stop words
	 * @return ArrayList<String> - the tokens (of just content words) of the web page text
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
	 * @return tokenToCountMap
	 */
	public HashMap<String, Integer> createTokenToCountMap() {	
		ArrayList<String> tempTokens = tokenizer(userWords);
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
		//		for (String s : stopWordsArrayList) {
		//			tokenToCountMap.remove(s);
		//		}

		//TESTING
		System.out.println();
		System.out.println("tokenToCountMap: " + tokenToCountMap);
		return tokenToCountMap;
	}

	/**
	 * Takes tokens array list and tags each token for POS to then returns an array list of the lemmas of each content word.
	 * The lemmas are used for outputting the most frequent content words of the web page. 
	 * @return lemmaArrayList an array list of lemmas (of content words only)
	 */
	public ArrayList<String> lemmatizer() {
		ArrayList<String> tempTokensArrayList = tokenizer(userWords);

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
	 * Uses Array List of lemma created in the lemmatizer method to create a map of the lemma and their frequency and 
	 * then sorts that by converting it to a list and comparing the values in order to return the top n lemma
	 * @return topLemmaToCountList list of top n lemma and their frequency of occurrence
	 */
	public List<Entry<String, Long>> findTopLemma() {
		Map<String, Long> tempMap = lemmaArrayList.stream()
				.collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		topLemmaToCountList = tempMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				//limit is hard-coded for how many top words you want returned
				.limit(5)
				.collect(Collectors.toList());

		//TESTING
		System.out.println();
		System.out.println("Top 5 content words in this article: " + topLemmaToCountList);
		return topLemmaToCountList;
	}

	/**
	 * Extracts the people's names from the web page and calculates the probability of accuracy
	 * @return peopleInArticleArrayList list of the people in the article
	 */
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
			
			/*
			 * TRYING to get full names out -- 
			 * but it's still hard/inaccurate anyway because "Elizabeth Warren" and "Warren" get counted separately but
			 * it's typical to have the two used interchangeably
			 */
//			ArrayList<String> idklist = new ArrayList<>();
//			String idk = "";
//			for(Span s: nameSpans){
////	            System.out.print(s.toString());
//	            System.out.print("  :  ");
//	            // s.getStart() : contains the start index of possible name in the input string array
//	            // s.getEnd() : contains the end index of the possible name in the input string array
//	            for(int i = s.getStart(); i <s.getEnd(); i++){
//	            	idk = tokens[i];
//	                System.out.print(tokens[i] + " ");
//	            }
//	            
//	            idklist.add(idk);
//	            System.out.println("IDK: ");
//	            for (String idkstr : idklist) {
//	            	System.out.println(idkstr);
//	            }
//	            
//			}
			
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

	/**
	 * Uses Array List of people created in the findPeople method to create a map of the people mentioned in the web page 
	 * and their frequency and then sorts that by converting it to a list and comparing the values in order to return the top n people
	 * @return topPeopleToCountList list of top n people and their frequency of occurrence
	 */
	public List<Entry<String, Long>> findTopPeople() {
		Map<String, Long> tempMap = peopleInArticleArrayList.stream()
				.collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		topPeopleToCountList = tempMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				//limit is hard-coded for how many top people you want returned
				.limit(3)
				.collect(Collectors.toList());

		//TESTING
		System.out.println();
		System.out.println("Top 3 most commonly mentioned people: " + topPeopleToCountList);

		return topPeopleToCountList;

	}

//	/**
//	 * Getter methods for the following instance variables: userWords, lemmaArrayList, tokenToCountMap
//	 * @return
//	 */
//	public String getUserWords() {
//		return userWords;
//	}
//
//	public ArrayList<String> getLemmaArrayList() {
//		return lemmaArrayList;
//	}

	public HashMap<String, Integer> getTokenToCountMap() {
		return tokenToCountMap;
	}

	public static void main(String args[]) {
		NLPData nlp = new NLPData();

		nlp.createTokenToCountMap();
		nlp.lemmatizer();
		nlp.findTopLemma();
		nlp.findPeople();
		nlp.findTopPeople();

		sentimentAnalysis sA = new sentimentAnalysis(nlp.getTokenToCountMap());
	}

}
