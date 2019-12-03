import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

class NLPDataTest {

	/*
	 * how to do JUnit testing with dynamic data? Is it okay to test the methods with hard-coded text?
	 */
	@Test
	void testTokenizer() {
//		NLPData nlp = new NLPData();
		ArrayList<String> tokenArrayList = new ArrayList<String>();
		String str = "This is a test string. This is testing the tokenizer method.";
		try {
			FileInputStream tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			String[] tokensArray = tokenizer.tokenize(str.toLowerCase());
			for(String token : tokensArray) {
				tokenArrayList.add(token);
			}
		} catch (IOException e) {
			System.out.println("Tokens model did not load properly.");
			e.printStackTrace();
		}
		
		ArrayList<String> stopWordsArrayList = new ArrayList<String>();
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

		tokenArrayList.removeAll(stopWordsArrayList);
		
		ArrayList<String> expectedAnswer = new ArrayList<String>(Arrays.asList("test", "string", "testing", "tokenizer", "method"));

//		assertEquals(nlp.getTokenArrayList(), expectedAnswer);
		assertEquals(tokenArrayList, expectedAnswer);

	}

	@Test
	void testCreateTokenToCountMap() {
		fail("Not yet implemented");
	}

	@Test
	void testLemmatizer() {
		fail("Not yet implemented");
	}

	@Test
	void testFindTopLemma() {
		fail("Not yet implemented");
	}

	@Test
	void testFindPeople() {
		fail("Not yet implemented");
	}

	@Test
	void testFindTopPeople() {
		fail("Not yet implemented");
	}

	@Test
	void testSentenceDetector() {
		fail("Not yet implemented");
	}

}
