import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
 
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
/**
* @author tutorialkart
*/
public class SentenceDetectExample {
 
	/**
     * This method is used to detect sentences in a paragraph/string
     * @throws InvalidFormatException
     * @throws IOException
     */
	public static void main(String[] args) {
        try {
            new SentenceDetectExample().sentenceDetect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public void sentenceDetect() throws InvalidFormatException, IOException {
        String paragraph = "Apache openNLP supports the most common NLP tasks, such as tokenization, sentence segmentation, part-of-speech tagging, named entity extraction, chunking, parsing, and coreference resolution. These tasks are usually required to build more advanced text processing services. OpenNLP also includes maximum entropy and perceptron based machine learning.";
 
        // refer to model file "en-sent,bin", available at link http://opennlp.sourceforge.net/models-1.5/
        InputStream is = new FileInputStream("en-sent.bin");
        SentenceModel model = new SentenceModel(is);
 
        // load the model
        SentenceDetectorME sdetector = new SentenceDetectorME(model);
 
        // detect sentences in the paragraph
        String sentences[] = sdetector.sentDetect(paragraph);
 
        // print the sentences detected, to console
        for(int i=0;i<sentences.length;i++){
            System.out.println(sentences[i]);
        }
        is.close();
    }
}