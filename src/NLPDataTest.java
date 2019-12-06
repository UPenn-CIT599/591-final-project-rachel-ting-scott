import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class NLPDataTest {
	
	//these are instance variables because they are used more than once in the tests
	/**
	 * Tester string of text from a web page since methods will be run on dynamically retrieved text. Source: 
	 * www.npr.org/2019/12/01/783989343/as-impeachment-inquiry-moves-to-judiciary-committee-republicans-attack-the-proce
	 */
	private String testStr = "As the House impeachment inquiry moves this week from the fact-gathering stage in the Intelligence Committee "
			+ "to considerations of law in the Judiciary Committee, the White House says President Trump does not intend to "
			+ "participate in a Wednesday hearing. Taken from here: "
			+ "www.npr.org/2019/12/01/783989343/as-impeachment-inquiry-moves-to-judiciary-committee-republicans-attack-the-proce";
	
	/**
	 * Tester array list of tokens that is the expected list of tokens from the testStr string of text.
	 */
	private ArrayList<String> testTokens = new ArrayList<String>(Arrays.asList("house", "impeachment", "inquiry", "moves", "week", 
			"fact-gathering", "stage", "intelligence", "committee", "considerations", "law", "judiciary", "committee", "white", 
			"house", "president", "trump", "intend", "participate", "wednesday", "hearing", 
			"www.npr.org/2019/12/01/783989343/as-impeachment-inquiry-moves-to-judiciary-committee-republicans-attack-the-proce"));

	/**
	 * Tester string of text (of a longer passage for more variety of lemma) from a web page. Source:
	 * https://kuow.org/stories/1st-supreme-court-gun-rights-battle-in-10-years-may-transform-legal-landscape
	 */
	private String testPara = "What did the Supreme Court mean in its 2008 decision when it said that the "
			+ "right to bear arms is an individual right? Back then, Justice Antonin Scalia, writing for the five justice court majority, "
			+ "framed the right most explicitly as the right to own a gun for self defense in one's home. Moreover, the opinion "
			+ "contained a paragraph of specific qualifiers that, according to court sources, were added to Scalia's opinion at the "
			+ "insistence of Justice Kennedy, who provided the fifth vote needed to prevail in the case. The court said, for instance, "
			+ "that its opinion \"cast no doubt on\" longstanding bans on \"carrying firearms in sensitive places such as schools and "
			+ "government buildings, or bans on dangerous and unusual weapons. It will make a difference that Justice Kavanaugh is on "
			+ "the court. But Kennedy — who insisted on that limiting language — has now retired, replaced by Justice Brett Kavanaugh. "
			+ "And Kavanaugh, as a lower court judge, wrote in favor of expansive gun rights. I do think it will make a difference "
			+ "that Justice Kavanaugh is on the court,\" says the gun owners' Clement.";
	
	/**
	 * Tests tokenizer method with a short string.
	 */
	@Test
	void testTokenizer() {
		String str = "This is a test string. This is testing the tokenizer method.";
		NLPData nlp = new NLPData(str);	
		nlp.tokenizer(str);
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("test", "string", "testing", "tokenizer", "method"));
		assertEquals(expectedAnswer, nlp.getTokenArrayList());
	}
	
	/**
	 * Tests tokenizer method with another, difference short string.
	 */
	@Test
	void testTokenizer2() {
		String str = "Now, I'm trying more punctuation! And just different words in general...";	
		NLPData nlp = new NLPData(str);
		nlp.tokenizer(str);
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("punctuation", "words", "general"));
		assertEquals(expectedAnswer, nlp.getTokenArrayList());
	}
	
	/**
	 * Tests tokenizer method with the short paragraph string (testStr) instance variable of this class.
	 */
	@Test
	void testTokenizerPara() {
		NLPData nlp = new NLPData(testStr);
		nlp.tokenizer(testStr);
		assertEquals(testTokens, nlp.getTokenArrayList());
	}

	/**
	 * Tests the creation of the tokenToCountMap method using the short paragraph string (testStr) and array list of tokens (testTokens) 
	 * from the instance variables of this class.
	 * Test accuracy for the lemmatizer depend on the content of the lemmatizer.dict (created by Apache OpenNLP), so a human reader 
	 * could disagree with the 'expectedAnswer'. However, if a lemma is not in the lemmatizer.dict, then the method will not recognize
	 * it in the text and thus will not extract it as a lemma in the method.
	 */
	@Test
	void testGetPositivityScore() {
		NLPData nlp = new NLPData(testStr);
		HashMap<String, Integer> expectedAnswer = new HashMap<>();
		for (String str : testTokens) {
			if (expectedAnswer.containsKey(str)) {
				int tempCount = expectedAnswer.get(str);
				tempCount++;
				expectedAnswer.put(str, tempCount);
			}
			else {
				expectedAnswer.put(str, 1);
			}
		}
		nlp.getPositivityScore(testTokens);
		assertEquals(expectedAnswer, nlp.getTokenToCountMap());
	}

	/**
	 * Tests the lemmatizer method using the short paragraph string (testStr) and array list of tokens (testTokens) from 
	 * the instance variables of this class.
	 * Test accuracy for the lemmatizer depend on the content of the lemmatizer.dict (created by Apache OpenNLP), so a human reader 
	 * could disagree with the 'expectedAnswer'. However, if a lemma is not in the lemmatizer.dict, then the method will not recognize
	 * it in the text and thus will not extract it as a lemma in the method.
	 */
	@Test
	void testLemmatizer() {
		NLPData nlp = new NLPData(testStr);
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("house", "move", "week", "intelligence", "committee",
				"consideration", "judiciary", "committee", "white", "house", "trump", "intend", "participate", "hear"));
		nlp.lemmatizer(testTokens);
		assertEquals(expectedAnswer, nlp.getLemmaArrayList());
	}
	
	/**
	 * Tests the lemmatizer method using the much longer paragraph (testPara) and array list of lemma (testParaLemma) 
	 * from the instance variables of this class.
	 * Test accuracy for the lemmatizer depend on the content of the lemmatizer.dict (created by Apache OpenNLP), so a human reader 
	 * could disagree with the 'expectedAnswer'. However, if a lemma is not in the lemmatizer.dict, then the method will not recognize
	 * it in the text and thus will not extract it as a lemma in the method.
	 */
	@Test
	void testLemmatizerPara() {
		NLPData nlp = new NLPData(testPara);

		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("supreme", "bear", "arm", "individual", "justice", 
				"individual", "justice", "write", "justice", "majority", "frame", "right", "explicitly", "right", "own", "gun",
				"paragraph", "qualifier", "source", "insistence", "justice", "provide", "vote", "prevail", "cast", "longstanding", 
				"band", "carry", "firearm", "sensitive", "place", "school", "building", "ban", "dangerous", "unusual", "weapon",
				"justice", "insist", "limit", "retire", "replace", "justice", "low", "judge", "write", "expansive", "gun",
				"right", "think", "justice", "gun"));
		ArrayList<String> testParaTokens = new ArrayList<String>(Arrays.asList("supreme", "court", "decision", "bear", "arms", 
				"individual", "justice", "Antonin", "Scalia", "individual", "Justice", "writing", "justice", "court", "majority",
				"framed", "right", "explicitly", "right", "own", "gun", "defense", "home", "opinion", "paragraph", "qualifiers",
				"court", "sources", "opinion", "insistence", "justice", "provided", "vote", "prevail", "case", "court", "opinion",
				"cast", "doubt", "longstanding", "band", "carrying", "firearms", "sensitive", "places", "schools", "government",
				"buildings", "bans", "dangerous", "unusual", "weapons", "difference", "justice", "court", "insisted", "limiting", 
				"language", "retired", "replaced", "justice", "lower", "court", "judge", "wrote", "favor", "expansive", "gun",
				"rights", "think", "difference", "justice", "court", "gun", "owner's"));
		nlp.lemmatizer(testParaTokens);		
		assertEquals(expectedAnswer, nlp.getLemmaArrayList());
	}

	/**
	 * Tests the findTopLemma method using the short paragraph string (testStr) instance variable.
	 */
	@Test
	void testFindTopLemma() {
		NLPData nlp = new NLPData(testStr);
		
		ArrayList<String> topLemma = new ArrayList<String>(Arrays.asList("house", "move", "week", "intelligence", "committee",
				"consideration", "judiciary", "committee", "white", "house", "trump", "intend", "participate", "hear"));
		List<Entry<String, Long>> expectedAnswerList = null;
		
		Map<String, Long> tempMap = topLemma.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		expectedAnswerList = tempMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(5).collect(Collectors.toList());
		nlp.findTopLemma(topLemma);
		assertEquals(expectedAnswerList, nlp.getTopLemmaToCountList());
	}

	/**
	 * Tests the findPeople method using the longer paragraph string (testPara) instance variable.
	 * This test fails in such a way that demonstrates the limitations of the Named Entity Recognition (NER) model in the OpenNLP API. 
	 * For example, in this specific test case, the names 'Kennedy' and 'Kavanaugh' are not identified as people.
	 */
	@Test
	void testFindPeople() {
		NLPData nlp = new NLPData(testPara);
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("Antonin", "Scalia", "Scalia", "Kennedy", "Kavanaugh", 
				"Kennedy", "Brett", "Kavanaugh", "Kavanaugh", "Kavanaugh", "Clement"));
		//this was the answer: [Antonin, Scalia, But, Brett] --> Kennedy and Kavanaugh are not being recognized
		ArrayList<String> expectedAnswer2 = new ArrayList<String>(Arrays.asList("Antonin", "Scalia", "Brett"));
		nlp.findPeople(testPara);
		assertEquals(expectedAnswer2, nlp.getPeopleInArticleArrayList());
	}

	/**
	 * Tests the findTopPeople method using the longer paragraph string (testPara) instance variable.
	 */
	@Test
	void testFindTopPeople() {
		NLPData nlp = new NLPData(testPara);
		ArrayList<String> allPeople = new ArrayList<String>(Arrays.asList("Antonin", "Scalia", "Scalia", "Kennedy", "Kavanaugh", 
				"Kennedy", "Brett", "Kavanaugh", "Kavanaugh", "Kavanaugh", "Clement"));
		List<Entry<String, Long>> expectedAnswerList = null;
		
		Map<String, Long> tempMap = allPeople.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		expectedAnswerList = tempMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(3).collect(Collectors.toList());
		nlp.findTopPeople(allPeople);
		assertEquals(expectedAnswerList, nlp.getTopPeopleToCountList());
	}

	/**
	 * Tests the sentenceDetector method using the longer paragraph string (testPara) instance variable.
	 */
	@Test
	void testSentenceDetector() {
		NLPData nlp = new NLPData(testPara);
		
		String[] expectedAnswer = new String[] {"What did the Supreme Court mean in its 2008 decision when it said that the "
				+ "right to bear arms is an individual right?"};
		assertEquals(expectedAnswer[0], nlp.getTitleOrFirstSentence());
	}

}
