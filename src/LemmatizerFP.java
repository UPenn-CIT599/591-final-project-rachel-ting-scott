import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.*;
 
/**
* Dictionary LemmatizerFP Example in Apache OpenNLP
*/
public class LemmatizerFP {
 
    public static void main(String[] args){
    	InputStream tokenModelIn = null;
        InputStream posModelIn = null;
        InputStream dictLemmatizer = null;
    	
        try{
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
 
            // loading the dictionary to input stream
//            InputStream dictLemmatizer = new FileInputStream("dictionary"+File.separator+"en-lemmatizer.txt");
            dictLemmatizer = new FileInputStream("en-lemmatizer.dict");

            // loading the lemmatizer with dictionary
            DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
 
            // finding the lemmas
            String[] lemmas = lemmatizer.lemmatize(tokens, tags);
 
            // printing the results
            System.out.println("\nPrinting lemmas for the given sentence...");
            System.out.println("WORD -POSTAG : LEMMA");
            for(int i = 0; i < tokens.length; i++){
                System.out.println(tokens[i]+" -"+tags[i]+" : "+lemmas[i]);
            }
 
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}