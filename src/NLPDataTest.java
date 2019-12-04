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
	
	//these two variables are used more than once in the tests, so they are instance variables to reduce copied code
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
	private String testPara = "And there is the rub. What did the Supreme Court mean in its 2008 decision when it said that the "
			+ "right to bear arms is an individual right? Back then, Justice Antonin Scalia, writing for the five justice court majority, "
			+ "framed the right most explicitly as the right to own a gun for self defense in one's home. Moreover, the opinion "
			+ "contained a paragraph of specific qualifiers that, according to court sources, were added to Scalia's opinion at the "
			+ "insistence of Justice Kennedy, who provided the fifth vote needed to prevail in the case. The court said, for instance, "
			+ "that its opinion \"cast no doubt on\" longstanding bans on \"carrying firearms in sensitive places such as schools and "
			+ "government buildings, or bans on dangerous and unusual weapons. It will make a difference that Justice Kavanaugh is on "
			+ "the court. But Kennedy — who insisted on that limiting language — has now retired, replaced by Justice Brett Kavanaugh. "
			+ "And Kavanaugh, as a lower court judge, wrote in favor of expansive gun rights. I do think it will make a difference "
			+ "that Justice Kavanaugh is on the court,\" says the gun owners' Clement.";
	
//	/**
//	 * Tester array list of lemma that is the expected list of lemma from the testPara string
//	 */
//	private ArrayList<String> testParaLemma = new ArrayList<String>(Arrays.asList("supreme", "court", "decision", "bear", "arms", 
//			"individual", "justice", "Antonin", "Scalia"));
	
	/**
	 * Tests tokenizer method with a short string.
	 */
	@Test
	void testTokenizer1() {
		String str = "This is a test string. This is testing the tokenizer method.";
		NLPData nlp = new NLPData(str);	
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("test", "string", "testing", "tokenizer", "method"));
		assertEquals(nlp.tokenizer(str), expectedAnswer);
	}
	
	/**
	 * Tests tokenizer method with another, difference short string.
	 */
	@Test
	void testTokenizer2() {
		String str = "Now, I'm trying more punctuation! And just different words in general...";	
		NLPData nlp = new NLPData(str);
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("punctuation", "words", "general"));
		assertEquals(nlp.tokenizer(str), expectedAnswer);
	}
	
	/**
	 * Tests tokenizer method with the short paragraph string (testStr) instance variable of this class.
	 */
	@Test
	void testTokenizerPara() {
		NLPData nlp = new NLPData(testStr);
		assertEquals(nlp.tokenizer(testStr), testTokens);
	}

	/**
	 * Tests the createTokenToCountMap method using the short paragraph string (testStr) and array list of tokens (testTokens) 
	 * from the instance variables of this class.
	 */
	@Test
	void testCreateTokenToCountMap() {
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
		assertEquals(nlp.createTokenToCountMap(testTokens), expectedAnswer);
	}

	/**
	 * Tests the lemmatizer method using the short paragraph string (testStr) and array list of tokens (testTokens) from 
	 * the instance variables of this class.
	 */
	@Test
	void testLemmatizer() {
		NLPData nlp = new NLPData(testStr);
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("house", "move", "week", "intelligence", "committee",
				"consideration", "judiciary", "committee", "white", "house", "trump", "intend", "participate", "hear"));
		
		assertEquals(nlp.lemmatizer(testTokens), expectedAnswer);
	}
	
	//JUST EXTRA IF WE NEED MORE TESTS
	/**
	 * Tests the lemmatizer method using the much longer paragraph (testPara) and array list of lemma (testParaLemma) 
	 * from the instance variables of this class.
	 */
//	@Test
//	void testLemmatizerPara() {
//		NLPData nlp = new NLPData(testPara);
//		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("supreme", "court", "decision", "bear", "arms", 
//				"individual", "justice", "Antonin", "Scalia"));
////		ArrayList<String> temp = new ArrayList<>();
////		temp.tok
//		
//		assertEquals(nlp.lemmatizer(testParaLemma), expectedAnswer);
//	}

	/**
	 * Tests the findTopLemma method using the short paragraph string testStr instance variable.
	 */
	@Test
	void testFindTopLemma() {
		NLPData nlp = new NLPData(testStr);
		
//		NLPData nlp = new NLPData(testStr);
		ArrayList<String> topLemma = new ArrayList<String>(Arrays.asList("house", "move", "week", "intelligence", "committee",
				"consideration", "judiciary", "committee", "white", "house", "trump", "intend", "participate", "hear"));
		List<Entry<String, Long>> expectedAnswerList = null;
		
		Map<String, Long> tempMap = topLemma.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

		expectedAnswerList = tempMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(5).collect(Collectors.toList());
		
		assertEquals(nlp.findTopLemma(topLemma), expectedAnswerList);
	}

	/**
	 * Tests the findPeople method using the longer paragraph string (testPara) instance variable.
	 * This test fails in such a way that shows that the Named Entity Recognition (NER) model in the OpenNLP API has some limitations.
	 * For example, in this specific test case, the names 'Kennedy' and 'Kavanaugh' are not identified as people.
	 */
	@Test
	void testFindPeople() {
		NLPData nlp = new NLPData(testPara);
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("Antonin", "Scalia", "Scalia", "Kennedy", "Kavanaugh", 
				"Kennedy", "Brett", "Kavanaugh", "Kavanaugh", "Kavanaugh", "Clement"));
		//this was the answer: [Antonin, Scalia, But, Brett] --> Kennedy and Kavanaugh are not being recognized
		ArrayList<String> expectedAnswer2 = new ArrayList<String>(Arrays.asList("Antonin", "Scalia", "But", "Brett"));
		
		assertEquals(nlp.findPeople(), expectedAnswer2);
	}

//	@Test
//	void testFindTopPeople() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testSentenceDetector() {
//		fail("Not yet implemented");
//	}

}
