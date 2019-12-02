

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class Summarizer {
	String languageCode = "EN";
	int lengthOfSummary = 3;
	String[] sentences = null;
	String userWords;
	ArrayList<String> tokenArrayList;
	HashMap<String, Integer> tokenToCountMap;
	private static ArrayList<String> maxWordList = new ArrayList<>();	
	private int belongsToSentenceN;
	
	public Summarizer() {
		NLPData nlp = new NLPData();
		this.userWords = nlp.getUserWords();
		nlp.tokenizer(this.userWords);
		this.tokenArrayList = nlp.getTokenArrayList();
		this.tokenToCountMap = nlp.getTokenToCountMap();
		this.belongsToSentenceN = belongsToSentenceN;
	}

	/**
	 * This method is used to detect sentences in a paragraph/string
	 */
	//MOVE THIS METHOD TO THE NLPDATA CLASS?????
	public String[] sentenceDetector() {
//		String paragraph = "This is a statement. This is another statement. Now is an abstract word for time, that is always flying.";
		
		try {
		InputStream sentenceModelIn = new FileInputStream("en-sent.bin");
		SentenceModel sentenceModel = new SentenceModel(sentenceModelIn);

		// feed the model to SentenceDetectorME class
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

		// detect sentences in the paragraph
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
	
	public String[] getSentences() {
		return sentences;
	}
	
	
	public StringBuilder writeSummary() {		
		ArrayList<String> sentenceArrayList = new ArrayList<>();
		
		for (String sentence : sentences) {
			sentenceArrayList.add(sentence);
		}
//		ArrayList<String> sentenceList = sentenceBuilder.getSentenceObjects();
//		ArrayList<Word> wordList = WordBuilder.getMaxWordList();
		
		StringBuilder textSummary = new StringBuilder();
		for (int i = 0; i < userWords.length(); i++) {
			int index = userWords.get(i).getBelongingSentenceNo();
			textSummary.append(sentenceList.get(index).getText() + " ");
		}
		return textSummary;
	}
	
	
	
	/**
	 * Find top n words that occur the most in the document. Every word is from a different sentence. 
	 * @param nTopEntries number of words to find
	 */
	public void findTopNWords(int nTopEntries /* boolean nonConsecutive*/){
		if(nTopEntries == 0){
			System.err.println("Can't be 0.");
			return;
		}
		else if(nTopEntries > tokenToCountMap.size()){
			System.err.println("Entry number higher than number of total entries.");
			System.err.println("Aborting...");
			return;
		}
			
		
		String tempWord = null;
		ArrayList<String> tempWordArrayList = new ArrayList<>();
		tempWordArrayList.addAll(tokenToCountMap.keySet());
		
//		List<Word> keys = new ArrayList<>(tokenToCountMap.keySet());
		

		
		// hente ut n antall entries
		int counter=0;
		while(counter!=nTopEntries){
			String maxWord = null;
			
			// loop gjennom listen for � finne maks
			for (String str : tempWordArrayList) {
				if (tempWord == null || maxWord.compareTo(tempWord) > 0)
			    {
					tempWord = maxWord;
					
					
			    }
			}
			// after finding top word for this run in the for-loop
			maxWord = tempWord;
			
			// add word to final result-list only if there isn't already
			// a word that belongs to the same sentence
			if(!checkSameSentenceNo(maxWordList, maxWord)){
				maxWordList.add(maxWord);
				counter++;
			}
			
			//remove max-word to find second max-word and so on..
			tempWordArrayList.remove(maxWord);		
			//reset 
			tempWord = null;			
			maxWord = null;
			
			
		}		
	}
	
	/**
	 * Check if entries in list has same sentence no.
	 * @param list the list we're checking against
	 * @param w Word
	 * @return true if thers's already a word with same sentence no.
	 */
	private boolean checkSameSentenceNo(ArrayList<String> list, String str){
		if(str == null)
			return false;
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getBelongingSentenceNo() == str.getBelongingSentenceNo())
				return true;
		}
		
		return false;
	}
	
	public static ArrayList<String> getMaxWordList(){
		return maxWordList;
	}
	
	public int getBelongingSentenceNo(){
		return this.belongsToSentenceN;
	}
	
//	@Override
//	public String toString(){
//		return "No. " + this.sentenceNo + "\t Text: " + this.text;
//	}

	public static void main(String[] args) {
		Summarizer summary = new Summarizer();
		summary.sentenceDetector();
		
		
		
		//	String filePath = "files/file-medium_en.txt";
//		String languageCode = "EN";
//		int lengthOfSummary = 3;
//		NLPData nlp = new NLPData();
//		String userWords = nlp.getUserWords();
//		nlp.tokenizer(userWords);
//		ArrayList<String> tokenArrayList = nlp.getTokenArrayList();


		//	SentenceBuilder sentenceBuilder = new SentenceBuilder(languageCode, filePath);
		//	WordBuilder wordBuilder = new WordBuilder();
		//	wordBuilder.getWords(languageCode, filePath);
		//	wordBuilder.removeStopWords(languageCode);
		//	wordBuilder.doCount(WordBuilder.getCleanWordObjects());
		//	wordBuilder.findTopNWords(lengthOfSummary);
		//	
		//	ArrayList<Sentence> sentenceList = sentenceBuilder.getSentenceObjects();
		//	ArrayList<Word> wordList = WordBuilder.getMaxWordList();
		//	StringBuilder textSummary = new StringBuilder();
		//	for (int i = 0; i < wordList.size(); i++) {
		//		int index = wordList.get(i).getBelongingSentenceNo();
		//		textSummary.append(sentenceList.get(index).getText() + " ");
		//	}
		//	System.out.println(textSummary);
		//	System.out.println(wordList);

	}

}
