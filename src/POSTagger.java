import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * This class modified tutorial code from www.tutorialkart.com for the specific purposes of this project
 * This class uses Apache OpenNLP to take in a String, tokenize it (split it into individual words, 
 * separate punctuation), and then label each token with their part of speech POS (see README for POS key)
 * and their probability of occurring.
 */
public class POSTagger {

	public static void main(String[] args) {

		InputStream tokenModelIn = null;
		InputStream posModelIn = null;

		try {
			String sentence = "John is 26 years old. He is older than me, but I am smarter than him. "
					+ "sad sadly sadder saddest slow slowly slower slowest not happy the news my car there are no cars. "
					+ "she doesn't want to because she does not want to; however, she should. Whose car is that? "
					+ "I want the teacher who is nice.";
			// tokenize the sentence
			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			String tokens[] = tokenizer.tokenize(sentence);

			// Parts-Of-Speech Tagging
			// reading parts-of-speech model to a stream
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			String tags[] = posTagger.tag(tokens);
			// Getting the probabilities of the tags given to the tokens
			double probs[] = posTagger.probs();

			System.out.println("Token\t:\tTag\t:\tProbability\n---------------------------------------------");
			for(int i=0;i<tokens.length;i++){
				System.out.println(tokens[i]+"\t:\t"+tags[i]+"\t:\t"+probs[i]);
			}

		}
		catch (IOException e) {
			// Model loading failed, handle the error
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
		}
	}
}