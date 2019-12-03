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
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class NLPData {
	/**
	 * My questions still:
	 * 1. do I need the finally blocks and null initializations for the files?
	 * 2. when reading in more than one file per method, is it okay if the try/catch covers both at same time but then message cannot
	 * say which file specifically wasn't read properly
	 * 3. List
	 * 4. String tokens[] equals String[] tokens ??
	 */
	/**
	 * Some definitions:
	 * token = text parsed as single words
	 * stop words = non-functional words in the English language (ex: 'the', 'and')
	 * POS = parts of speech (ex: noun, verb, adv, etc)
	 * lemma = the base form of a word in the dictionary form (ex: words --> word, types --> type)
	 * This class uses Apache OpenNLP to take in a String of text as input from the WebScraper class, tokenize it, remove stop words, 
	 * count the frequency of each token (for the purpose of the sentiment analysis), lemmatize the Tokens, 
	 * store those in a hashmap and pull out the top n content words for output,
	 * extract the names of people and store those in a hashmap and pull out the top n people mentioned for output
	 * In learning how to use OpenNLP methods and pre-trained models, tutorials from www.tutorialkart.com were used
	 */
	private InputStream tokenModelIn = null;
	private InputStream posModelIn = null;
	private InputStream dictLemmatizer = null;
	private InputStream entityModelIn = null;

	private String userWords;

	//Just use the following String variable when not connected to WebScraper.java
	//private String userWords = "The story goes like this. ADD MORE :)";	
	private ArrayList<String> stopWordsArrayList = new ArrayList<String>();
	private ArrayList<String> tokenArrayList;
	private ArrayList<String> lemmaArrayList;
	private HashMap<String, Integer> tokenToCountMap;
	private List<Map.Entry<String, Long>> topLemmaToCountList;
	private ArrayList<String> peopleInArticleArrayList;
	private List<Map.Entry<String, Long>> topPeopleToCountList;
	String[] sentences = null;
	private sentimentAnalysis sA;

	/**
	 * Constructor that creates an instance of the web scraper and sentiment analysis classes
	 */
	public NLPData() {
		//put the try/catch/finally for reading models and files into the constructor
				
		//MOVED TO tokenizer method
//		WebScraper webscraper = new WebScraper();
//		String out = webscraper.runScraper();
//		userWords = out;
		
//instantiate this variable in this method and then put the completed tokenToCount map as parameter once the method has run
		//as a global variable it'll get updated once the method is run, so perhaps delete the parameter as long as the sA is run after the 
		//create tokenToCountMap method is run
//		sA = new sentimentAnalysis(tokenToCountMap);
	}

	/**
	 * Helper method that takes the String of web page text, tokenizes it, and removes the stop words
	 * @return ArrayList<String> - the Tokens (of just content words) of the web page text
	 */
	public ArrayList<String> tokenizer(String str) {
		WebScraper webscraper = new WebScraper();
		String out = webscraper.runScraper();
		userWords = out;
		
		try {
			//reading OpenNLP Tokens model to a stream
			tokenModelIn = new FileInputStream("en-token.bin");
			//loading OpenNLP Tokens model from stream
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			//initializing the tokenizer with OpenNLP pre-trained model
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			//OpenNLP tokenize method for creating the Tokens and storing them in a String[]
			String[] tokensArray = tokenizer.tokenize(userWords.toLowerCase());
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
			//Model loading failed, handle the error
			System.out.println("Tokens model did not load properly.");
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
		//TESTING
//		System.out.println("Tokens Array List: " + tokenArrayList);
		return tokenArrayList;
	}

	/**
	 * Grabs all the Tokens; counts their frequency; stores those key-value pairs in a HashMap
	 * @return tokenToCountMap
	 */
	public HashMap<String, Integer> createTokenToCountMap() {	
//		ArrayList<String> tempTokenArrayList = tokenizer(userWords);
		ArrayList<String> tempTokenArrayList = getTokenArrayList();

		tokenToCountMap = new HashMap<>();
		for (String str : tempTokenArrayList) {
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
//		System.out.println("tokenToCountMap: " + tokenToCountMap);
		sA = new sentimentAnalysis(tokenToCountMap);
		return tokenToCountMap;
	}

	/**
	 * Takes Tokens array list and tags each token for POS to then returns an array list of the lemmas of each content word.
	 * The lemmas are used for outputting the most frequent content words of the web page. 
	 * @return lemmaArrayList an array list of lemmas (of content words only)
	 */
	public ArrayList<String> lemmatizer() {
		ArrayList<String> tempTokenArrayList = tokenizer(userWords);
//		ArrayList<String> tempTokenArrayList = getTokenArrayList();

		try {
			//reading OpenNLP parts-of-speech model to a stream
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			//loading OpenNLP parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			//initializing the parts-of-speech tagger with OpenNLP pre-trained model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			//OpenNLP tag method for tagging the Tokens (which takes in array, thus the conversion from array list to array
			String[] tempTokensArray = new String[tempTokenArrayList.size()]; 
			tempTokensArray = tempTokenArrayList.toArray(tempTokensArray); 			
			String[] tags = posTagger.tag(tempTokensArray);

			//loading the dictionary to input stream
			dictLemmatizer = new FileInputStream("en-lemmatizer.dict");

			//loading the lemmatizer with dictionary
			DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

			//OpenNLP lemmatize method for putting the lemma in a String[]
			String[] lemmas = lemmatizer.lemmatize(tempTokensArray, tags);

			//converting lemmas String[] to array list
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
			System.out.println("Lemma dictionary file not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Something went wrong.");
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
			if (dictLemmatizer != null) {
				try {
					dictLemmatizer.close();
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
//	            	idk = Tokens[i];
//	                System.out.print(Tokens[i] + " ");
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
				/*
				 * Since 'The' and 'I' are commonly the first word of a sentence (and for 'The' - thus capitalized), the NER-Person Model
				 * includes them as people's names, so removing here increases the accuracy of this method.
				 */				
				if (!name.equals("The") && (!name.equals("I"))) {
					peopleInArticleArrayList.add(name);
				}
			}			

			String namePlusProbability = "";
			System.out.println("\n------Name : Probability of Accuracy------");
			for (int i = 0; i < peopleInArticleArrayList.size(); i++) {
				for (int j = 0; j < nameSpans.length; j++) {
					namePlusProbability = peopleInArticleArrayList.get(i) + " : " + nameSpans[i].getProb();
				}
				System.out.println(namePlusProbability);
			}

		} catch (IOException e) {
			System.out.println("Oops, something went wrong. Check that all the files are properly included.");
			e.printStackTrace();
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
	
	/**
	 * Add note that this code for the future --what is does? why is it here? what is the plan for it?
	 * This method is used to detect sentences in a paragraph/string
	 */
	public String[] sentenceDetector() {		
		try {
		InputStream sentenceModelIn = new FileInputStream("en-sent.bin");
		SentenceModel sentenceModel = new SentenceModel(sentenceModelIn);

		//feed the model to SentenceDetectorME class
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

		//detect sentences in the paragraph
		sentences = sentenceDetector.sentDetect(userWords);

		//TESTING - print sentences detected
		System.out.println();
		System.out.println("-----Individual sentences from the web page-----");
		for(int i = 0; i < sentences.length; i++){
			System.out.println(sentences[i]);
		}
		sentenceModelIn.close();
		} catch (IOException e) {
			System.out.println("Sentence Detector Model not loading properly.");
		}
		return sentences;
	}

	/**
	 * Getter method for userWords
	 * @return userWords
	 */
	public String getUserWords() {
		return userWords;
	}

	/**
	 * Getter method for lemmaArrayList
	 * @return lemmaArrayList
	 */
	public ArrayList<String> getLemmaArrayList() {
		return lemmaArrayList;
	}

	/**
	 * Getter method for stopWordsArrayList
	 * @return stopWordsArrayList
	 */
	public ArrayList<String> getStopWordsArrayList() {
		return stopWordsArrayList;
	}

	/**
	 * Getter method for tokenArrayList
	 * @return tokenArrayList
	 */
	public ArrayList<String> getTokenArrayList() {
		return tokenArrayList;
	}

	/**
	 * Getter method for topLemmaToCountList
	 * @return topLemmaToCountList
	 */
	public List<Map.Entry<String, Long>> getTopLemmaToCountList() {
		return topLemmaToCountList;
	}

	/**
	 * Getter method for peopleInArticleArrayList
	 * @return peopleInArticleArrayList
	 */
	public ArrayList<String> getPeopleInArticleArrayList() {
		return peopleInArticleArrayList;
	}

	/**
	 * Getter method for topPeopleToCountList
	 * @return topPeopleToCountList
	 */
	public List<Map.Entry<String, Long>> getTopPeopleToCountList() {
		return topPeopleToCountList;
	}

	/**
	 * Getter method for tokenToCountMap
	 * @return tokenToCountMap
	 */
	public HashMap<String, Integer> getTokenToCountMap() {
		return tokenToCountMap;
	}
	
	/**
	 * Getter method for sentences
	 * @return sentences
	 */
	public String[] getSentences() {
		return sentences;
	}

	public static void main(String args[]) {
		NLPData nlp = new NLPData();
		nlp.lemmatizer();
		nlp.findTopLemma();
		nlp.findPeople();
		nlp.findTopPeople();
		nlp.createTokenToCountMap();
		nlp.sentenceDetector();

	}

}
