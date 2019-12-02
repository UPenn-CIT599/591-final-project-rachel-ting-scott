

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokens {
	private InputStream tokenModelIn = null;
	
//	private ArrayList<String> stopWordsArrayList;
//	private ArrayList<String> tokenObjectsList;
	
	/**
	 * Tokenizes and removes stop words from a string 
	 * @param str String of text from web page
	 */
//	public Tokens(String str) {
	public Tokens(String str) {
		NLPData nlp = new NLPData();
		ArrayList<String> tokens = nlp.tokenizer(nlp.getUserWords());
		//TESTING
//		for (String token : tokens) {
//			System.out.println(token);
//		}
	}
	
		
//		try {
//			//reading OpenNLP Tokens model to a stream
//			tokenModelIn = new FileInputStream("en-token.bin");
//			//loading OpenNLP Tokens model from stream
//			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
//			//initializing the tokenizer with OpenNLP pre-trained model
//			Tokenizer tokenizer = new TokenizerME(tokenModel);
//			//OpenNLP tokenize method for creating the Tokens and storing them in a String[]
//			String[] tokensArray = tokenizer.tokenize(str.toLowerCase());
//			tokenArrayList = new ArrayList<String>();
//			for(String token : tokensArray) {
//				tokenArrayList.add(token);
//			}
//
//			//use when not connected to WebScraper.java
//			//			tokensArray = tokenizer.tokenize(userWords.toLowerCase());
//			//			//TESTING
//			//			System.out.println("tokensArray:");
//			//			for (String element : tokensArray) {
//			//				System.out.print(element + "\t");
//			//			}
//
//		} catch (IOException e) {
//			//Model loading failed, handle the error
//			System.out.println("Tokens model did not load properly.");
//			e.printStackTrace();
//		} 
//
//		//remove stop word from userWords using the stop-words-en.txt list of custom stop words
//		try {
//			File readStopWords = new File("stop-words-en.txt");
//			Scanner sc = new Scanner(readStopWords);
//			while (sc.hasNext()) {
//				String stopWord = sc.next();
//				stopWordsArrayList = new ArrayList<String>();
//				stopWordsArrayList.add(stopWord);
//			}
//			sc.close();
//		} catch (FileNotFoundException e) {
//			System.out.println("Stop Word file is not found.");
//			e.printStackTrace();
//		}
//
//		finally {
//			if (tokenModelIn != null) {
//				try {
//					tokenModelIn.close();
//				}
//				catch (IOException e) {
//				}
//			}
//		}
//		tokenArrayList.removeAll(stopWordsArrayList);
//		System.out.println(tokenArrayList);
//	}
	
//	public static void main(String[] args) {
////		FileReader fr = new FileReader("webPageTextAsFile.txt");
////		Scanner sc = new Scanner(fr);
////		String webPageTextAsString = sc.next();
//		Tokens tokens = new Tokens();
//	}

}
