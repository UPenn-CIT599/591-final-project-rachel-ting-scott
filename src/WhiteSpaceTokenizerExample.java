import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
 
/**
* www.tutorialkart.com
* Tokenizer Example in Apache openNLP using WhitespaceTokenizer
*/
public class WhiteSpaceTokenizerExample {
 
    public static void main(String[] args) {
        Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
        String tokens[] = tokenizer.tokenize("John is 26 years old. He is older than me.");
 
        System.out.println("Token\n----------------");
        for(int i=0;i<tokens.length;i++){
            System.out.println(tokens[i]);
        }
    }
}