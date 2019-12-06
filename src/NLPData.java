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

/**
 * Relevant linguistics definitions:
 * Token = text parsed as single words.
 * Stop words = non-functional words in the English language (ex: 'the', 'and').
 * POS = parts of speech (ex: noun, verb, adv, etc).
 * Lemma = the base form of a word in the dictionary form (ex: words --> word, types --> type).
 * Named Entity Recognition (NER) = identifying a named entity, such as person or place, in a text.
 * 
 * This class uses Apache OpenNLP to take in a String of text as input from the WebScraper class, tokenize it, remove stop words, 
 * count the frequency of each token (for the purpose of the sentiment analysis), POS tag each token in order to lemmatize them, 
 * store those in a hashmap and pull out the top n content words for output, extract the names of people and store those in a 
 * hashmap and pull out the top n people mentioned for output. In learning how to use OpenNLP methods and pre-trained models, 
 * tutorials from www.tutorialkart.com were used.
 * @author rachelfriend, Ting-Hsuan Lee, Scott Theer
 */
public class NLPData {
	private String webPageText;
	private InputStream tokenModelIn;
	private Tokenizer tokenizer;
	private InputStream posModelIn;
	private POSTaggerME posTagger;
	private InputStream dictLemmatizer;
	private DictionaryLemmatizer lemmatizer;
	private NameFinderME nameFinder;
	private InputStream entityModelIn;
	private InputStream sentenceModelIn;
	private SentenceDetectorME sentenceDetector;

	//TESTING -- for when not connected to WebScraper or Runner
	//private String webPageText = "The story goes like this. ADD MORE :)";	
	private ArrayList<String> stopWordsArrayList;
	private ArrayList<String> tokenArrayList;
	private ArrayList<String> lemmaArrayList;
	private HashMap<String, Integer> tokenToCountMap;
	private int positivityScore;
	private List<Map.Entry<String, Long>> topLemmaToCountList;
	private ArrayList<String> peopleInArticleArrayList;
	private List<Map.Entry<String, Long>> topPeopleToCountList;
	String[] sentences;

	/**
	 * Constructor that initializes the instance variables and, in a try/catch/finally, reads in the required files and trained models 
	 */
	public NLPData(String text) {
		this.webPageText = text;
		stopWordsArrayList = new ArrayList<String>();
		tokenArrayList = new ArrayList<String>();
		lemmaArrayList = new ArrayList<String>();
		tokenToCountMap = new HashMap<String, Integer>();
		peopleInArticleArrayList  = new ArrayList<String>();

		//try-catch for Files
		try {
			File readStopWords = new File("stop-words-en.txt");
			Scanner sc = new Scanner(readStopWords);
			while (sc.hasNext()) {
				String stopWord = sc.next();
				stopWordsArrayList.add(stopWord);
			}
			sc.close();
			//loading the Apache OpenNLP lemma dictionary to input stream
			dictLemmatizer = new FileInputStream("en-lemmatizer.dict");

		} catch (FileNotFoundException e) {
			System.out.println("Oops. Something went wrong. A required file was not found.");
			e.printStackTrace();
		} 

		//try-catch for trained models
		try {
			//reading OpenNLP Tokens model to a stream
			tokenModelIn = new FileInputStream("en-token.bin");
			//loading OpenNLP Tokens model from the stream
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			//initializing the tokenizer with OpenNLP pre-trained model
			tokenizer = new TokenizerME(tokenModel);

			//reading OpenNLP parts-of-speech model to a stream
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			//loading OpenNLP parts-of-speech model from the stream
			POSModel posModel = new POSModel(posModelIn);
			//initializing the parts-of-speech tagger with OpenNLP pre-trained model
			posTagger = new POSTaggerME(posModel);

			//loading the lemmatizer with dictionary
			lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

			//reading OpenNLP named entity recognition-person model to a stream
			entityModelIn = new FileInputStream(new File("en-ner-person.bin"));
			//loading OpenNLP named entity recognition-person model from the stream
			TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(entityModelIn);
			//initializing the name finder with OpenNLP pre-trained model
			nameFinder = new NameFinderME(tokenNameFinderModel);

			//reading OpenNLP sentence detection model to a stream
			sentenceModelIn = new FileInputStream("en-sent.bin");
			//loading OpenNLP sentence model from the stream
			SentenceModel sentenceModel = new SentenceModel(sentenceModelIn);
			//initializing the sentence detector with OpenNLP pre-trained model
			sentenceDetector = new SentenceDetectorME(sentenceModel);

		} catch (IOException e) {
			System.out.println("Oops. Something went wrong. A model did not properly load.");
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
			if (dictLemmatizer != null) {
				try {
					dictLemmatizer.close();
				}
				catch (IOException e) {
				}
			}
			if (entityModelIn != null) {
				try {
					entityModelIn.close();
				}
				catch (IOException e) {

				}
			}
			if (sentenceModelIn != null) {
				try {
					sentenceModelIn.close();
				}
				catch (IOException e) {

				}
			}
		}
	}

	/**
	 * Helper method that takes the String of web page text, tokenizes it, and removes the stop words
	 * @param a string of text
	 */
	public void tokenizer(String str) {
		//OpenNLP tokenize method for creating the tokens and storing them in a String[]
		String[] tokensArray = tokenizer.tokenize(str.toLowerCase());
		for(String token : tokensArray) {
			tokenArrayList.add(token);
		}
		tokenArrayList.removeAll(stopWordsArrayList);
		//TESTING
		//System.out.println("Tokens Array List: " + tokenArrayList);
	}

	/**
	 * Grabs all the tokens; counts their frequency; stores those key-value pairs in a hash map; 
	 * initializes the sentimentAnalysis class and and runs its getScoreOutput method on the hash map.
	 * @param an array List of cleaned tokens (meaning the stop words have been removed)
	 */	
	public void getPositivityScore(ArrayList<String> cleanTokens) {	
		for (String str : cleanTokens) {
			if (tokenToCountMap.containsKey(str)) {
				int tempCount = tokenToCountMap.get(str);
				tempCount++;
				tokenToCountMap.put(str, tempCount);
			}
			else {
				tokenToCountMap.put(str, 1);
			}
		}
		sentimentAnalysis sA = new sentimentAnalysis(tokenToCountMap);
		positivityScore = sA.getScoreOutput();
	}

	/**
	 * Takes tokens array list and tags each token for POS to then returns an array list of the lemmas of each content word.
	 * The lemmas are used for outputting the most frequent content words of the web page. 
	 * @param an array List of cleaned tokens (meaning the stop words have been removed)
	 */	
	public void lemmatizer(ArrayList<String> cleanTokens) {
		//OpenNLP tag method for tagging the Tokens (which takes in array, thus the conversion from array list to array
		String[] tempTokensArray = new String[cleanTokens.size()]; 
		tempTokensArray = cleanTokens.toArray(tempTokensArray);
		String[] tags = posTagger.tag(tempTokensArray);

		//OpenNLP lemmatize method for putting the lemma in a String[]
		String[] lemmas = lemmatizer.lemmatize(tempTokensArray, tags);

		//converting lemmas String[] to array list
		for(String lemma : lemmas) {
			if (!lemma.equals("O")) {
				lemmaArrayList.add(lemma);
			}
		}	      
		//TESTING -- printing the results
		//System.out.println("LEMMALIST: " + lemmaArrayList);
		//System.out.println("\nPrinting lemmas for the given sentence...");
		//System.out.println("WORD -POSTAG : LEMMA");
		//for(int i = 0; i < tempTokens.length; i++){
		//	System.out.println(tempTokens[i]+" -"+tags[i]+" : "+lemmas[i]);
		//}
	}

	/**
	 * Uses Array List of lemma created in the lemmatizer method to create a map of the lemma and their frequency, sort that, 
	 * and then compare the values in order to return the top n lemma.
	 * @param an array List of cleaned lemma (meaning the stop words have been removed via the tokenizing that occurs before lemmanizing)
	 */
	public void findTopLemma(ArrayList<String> cleanLemma) {
		Map<String, Long> tempMap = cleanLemma.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		//limit is hard-coded for how many top words to be returned
		topLemmaToCountList = tempMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(5).collect(Collectors.toList());
		//TESTING
		//System.out.println();
		//System.out.println("Top 5 content words in this article: " + topLemmaToCountList);
	}

	/**
	 * Extracts people's names from the web page.
	 */
	public void findPeople(String str) {
		/*
		 * Must tokenize the webPageText again because stop words cannot be removed for the named entity recognition model and 
		 * methods to function properly.
		 */
		String tokens[] = tokenizer.tokenize(str);
		Span nameSpans[] = nameFinder.find(tokens);

		for (int i = 0; i < nameSpans.length; i++) {
			String name = tokens[nameSpans[i].getStart()];
			/*
			 * Since 'The', 'I', and 'But' are commonly the first word of a sentence (and thus capitalized), the NER-Person Model
			 * includes them as people's names, so removing here increases the accuracy of this method.
			 */				
			if (!name.equals("The") && (!name.equals("I")) && (!name.equals("But"))) {
				peopleInArticleArrayList.add(name);
			}
		}			
		//TESTING -- calculates and displays probability of accuracy in the extraction of people's names
		//		String namePlusProbability = "";
		//		System.out.println("\n------Name : Probability of Accuracy------");
		//		for (int i = 0; i < peopleInArticleArrayList.size(); i++) {
		//			for (int j = 0; j < nameSpans.length; j++) {
		//				namePlusProbability = peopleInArticleArrayList.get(i) + " : " + nameSpans[i].getProb();
		//			}
		//			System.out.println(namePlusProbability);
		//		}
	}		

	/**
	 * and then compare the values in order to return the top n lemma
	 * Uses Array List of people created in the findPeople method to create a map of the people mentioned and their frequency, 
	 * sort that, and then compare the values in order to return the top n people
	 * @param an array List of cleaned people tokens
	 */		
	public void findTopPeople(ArrayList<String> cleanPeople) {	
		Map<String, Long> tempMap = cleanPeople.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		//limit is hard-coded for how many top people you want returned
		topPeopleToCountList = tempMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(3).collect(Collectors.toList());

		//TESTING
		//		System.out.println();
		//		System.out.println("Top 3 most commonly mentioned people: " + topPeopleToCountList);
	}

	/**
	 * This method is used here to retrieve the title or first sentence of an article for the purpose of contextualizing the 
	 * analysis output.
	 * In general, this method utilizes an OpenNLP method to detect all sentences in a paragraph/string.
	 * It can be further developed to be more extensively used in the future after training a model to auto extract a summary of a text.
	 * @return the first sentence of a string of text
	 */
	public String getTitleOrFirstSentence() {	
		//detect sentences in the paragraph
		sentences = sentenceDetector.sentDetect(webPageText);

		//TESTING
		//		for(int i = 0; i < sentences.length; i++){
		//			System.out.println(sentences[i]);
		//		}
		return sentences[0];
	}

	/**
	 * Getter method for webPageText
	 * @return webPageText
	 */
	public String getWebPageText() {
		return webPageText;
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

	public int getPositivityScore() {
		return positivityScore;
	}

	//TESTING
//	public static void main(String args[]) {
//		String str = "ADD TESTING STRING HERE";
//		NLPData nlp = new NLPData(str);
//		nlp.tokenizer(nlp.getWebPageText());
//	}
	/*
	 * TESTING -- not ready for output
	 * TRYING to extract full names for the findPeople() method -- 
	 * but it's difficult, plus inaccurate anyway, because "Elizabeth Warren" and "Warren" get counted separately yet
	 * it's typical to have the two used interchangeably.
	 */
	//			ArrayList<String> idklist = new ArrayList<>();
	//			String idk = "";
	//			for(Span s: nameSpans){
	////	            System.out.print(s.toString());
	//	            System.out.print("  :  ");
	//	            // s.getStart() : returns the start index of name in the input string array
	//	            // s.getEnd() : returns the end index of name in the input string array
	//	            for(int i = s.getStart(); i <s.getEnd(); i++){
	//	            	idk = tokens[i];
	//	                System.out.print(tokens[i] + " ");
	//	            }          
	//	            idklist.add(idk);
	//			}
}
