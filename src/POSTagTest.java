import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

class POSTagTest {

	@Test
	void test() {
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize("John has a sister named Penny.");
		String[] tagsArray = null;
		// reading parts-of-speech model to a stream
		try {
			FileInputStream posModelIn = new FileInputStream("en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			tagsArray = posTagger.tag(tokens);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		String[] expectedAnswer = {"NNP", "VBZ", "DT", "NN", "VBN", "NNP", "."};
//		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
//		String[] tokens = tokenizer.tokenize("John has a sister named Penny.");
//
//		InputStream inputStreamPOSTagger = getClass()
//				.getResourceAsStream("/models/en-pos-maxent.bin");
//		POSModel posModel;
//		try {
//			posModel = new POSModel(inputStreamPOSTagger);
//			POSTaggerME posTagger = new POSTaggerME(posModel);
//			String tags[] = posTagger.tag(tokens);
//		} catch (IOException e) {
//			System.out.println("Files not found!");
//			e.printStackTrace();
//		}

		assertEquals(expectedAnswer, tagsArray, "Fail!" );
//		assertEquals("NNP", "VBZ", "DT", "NN", "VBN", "NNP", ".", nlpTest.tagger(), "Fail!" );
//		assertThat(tags).contains("NNP", "VBZ", "DT", "NN", "VBN", "NNP", ".");
	}

}
