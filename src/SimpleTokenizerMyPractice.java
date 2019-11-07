import java.util.HashMap;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
 
/**
* www.tutorialkart.com
* Tokenizer Example in Apache openNLP using SimpleTokenizer
*/
public class SimpleTokenizerMyPractice {
 
    public static void main(String[] args) {
    	HashMap<String, Integer> myMap = new HashMap<>();
    	
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String tokens[] = tokenizer.tokenize("John is 26 years old. He is older than me, but I am smarter than him.");
 
        System.out.println("Token\n----------------");
        for(int i=0;i<tokens.length;i++){
            System.out.println(tokens[i]);
        }
        
        for (String str : tokens) {
        	str = str.toLowerCase();
        	if (myMap.containsKey(str)) {
        		int temp = myMap.get(str);
        		temp++;
        		myMap.put(str, temp);
        	}
        	else {
        		myMap.put(str, 1);
        	}
        }
        System.out.println(myMap);
    }
}