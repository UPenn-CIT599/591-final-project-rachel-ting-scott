import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
 
/**
* www.tutorialkart.com
* Tokenizer Example in Apache openNLP using SimpleTokenizer
*/
public class SimpleTokenizerExample {
 
    public static void main(String[] args) {
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String tokens[] = tokenizer.tokenize("John is 26 years old.");
 
        System.out.println("Token\n----------------");
        for(int i=0;i<tokens.length;i++){
            System.out.println(tokens[i]);
        }
    }
}