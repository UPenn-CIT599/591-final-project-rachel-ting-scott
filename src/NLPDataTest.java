import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class NLPDataTest {

//	NLPData nlp = new NLPData();
	/*
	 * add more tests of just this one tokenizer if trouble meeting the 15 test minimum.
	 */
	@Test
	void testTokenizer1() {
		NLPData nlp = new NLPData();
		String str = "This is a test string. This is testing the tokenizer method.";	
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("test", "string", "testing", "tokenizer", "method"));
		assertEquals(nlp.tokenizer(str), expectedAnswer);
	}
	
	@Test
	void testTokenizer2() {
		NLPData nlp = new NLPData();
		String str = "Now, I'm trying more punctuation! And just different words in general...";	
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("punctuation", "words", "general"));
		assertEquals(nlp.tokenizer(str), expectedAnswer);
	}
	
	@Test
	void testTokenizerPara() {
		NLPData nlp = new NLPData();
		String str = "As the House impeachment inquiry moves this week from the fact-gathering stage in the Intelligence Committee "
				+ "to considerations of law in the Judiciary Committee, the White House says President Trump does not intend to "
				+ "participate in a Wednesday hearing. Taken from here: "
				+ "www.npr.org/2019/12/01/783989343/as-impeachment-inquiry-moves-to-judiciary-committee-republicans-attack-the-proce";
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("house", "impeachment", "inquiry", "moves", "week", 
				"fact-gathering", "stage", "intelligence", "committee", "considerations", "law", "judiciary", "committee", "white", 
				"house", "president", "trump", "intend", "participate", "wednesday", "hearing", 
				"www.npr.org/2019/12/01/783989343/as-impeachment-inquiry-moves-to-judiciary-committee-republicans-attack-the-proce"));
		assertEquals(nlp.tokenizer(str), expectedAnswer);
	}

	@Test
	/**
	 * for this test, please use this URL, but there is a limit to scraping it and the second they change their page, 
	 * this text won't work anymore:
	 * https://www.wikipedia.org/
	 * since HashMaps aren't sorted, how to do this? Use a .contains to check for expected answer???
	 */
	void testCreateTokenToCountMap() {
		NLPData nlp = new NLPData();
		HashMap<String, Integer> expectedAnswer = new HashMap<>();
		ArrayList<String> tempTokens = new ArrayList<String>(Arrays.asList("available", "save", "favorite", "articles", "read", "offline", "sync", 
				"reading", "lists", "devices", "customize", "reading", "experience", "official", "wikipedia", "app", "page", "available",
				"creative", "commons", "license", "terms", "privacy", "policy"));
//		ArrayList<String> tempTokens = new ArrayList<String>(Arrays.asList("house", "impeachment", "inquiry", "moves", "week", 
//				"fact-gathering", "stage", "intelligence", "committee", "considerations", "law", "judiciary", "committee", "white", 
//				"house", "president", "trump", "intend", "participate", "wednesday", "hearing", 
//				"www.npr.org/2019/12/01/783989343/as-impeachment-inquiry-moves-to-judiciary-committee-republicans-attack-the-proce"));
		for (String str : tempTokens) {
			if (expectedAnswer.containsKey(str)) {
				int tempCount = expectedAnswer.get(str);
				tempCount++;
				expectedAnswer.put(str, tempCount);
			}
			else {
				expectedAnswer.put(str, 1);
			}
		}
		assertEquals(nlp.createTokenToCountMap(), expectedAnswer);
	}

//	@Test
//	void testLemmatizer() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindTopLemma() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindPeople() {
//		fail("Not yet implemented");
//	}
//
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
