
import java.util.ArrayList;

public class Summarizer {
	public static void main(String[] args) {
//	String filePath = "files/file-medium_en.txt";
	String languageCode = "EN";
	int lengthOfSummary = 3;
	NLPData nlp = new NLPData();
	String userWords = nlp.getUserWords();

	
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
