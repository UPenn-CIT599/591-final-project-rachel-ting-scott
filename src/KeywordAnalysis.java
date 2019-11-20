import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.ml.maxent.io.GISModelWriter;
import opennlp.tools.ml.model.DataIndexer;
import opennlp.tools.ml.model.MaxentModel;

public class KeywordAnalysis {	
	private InputStream keywordModelIn = null;
	private String[] keywordArray;
	//or
	private ArrayList<String> keywordArrayList;
	
	/**
	 * 
	 */
	public static MaxentModel trainModel(DataIndexer di, int iterations) {
		
	}
	public void OnePassDataIndexer(EventStream events) {
		
	}
	File outputFile = new File(modelFileName+".bin.gz");
	GISModelWriter writer = new SuffixSensitiveGISModelWriter(model, outputFile);
	writer.persist();
	
	//load the model
	GISModel m = new SuffixSensitiveGISModelReader(new File(modelFileName)).getModel();
	
	/**
	 * Use our trained model to find keywords and thus identify the topic(s) of the text
	 * The dataset "nytimes.csv" from Kaggle.com was used to train the keywordAnalysis.bin model
	 * @return a array list of strings which are the top 3-4 keywords
	 */
	public ArrayList<String> nlpKeywordTagger() {
		NLPData nlpData = new NLPData();
		ArrayList<String> keywordsTEMP = new ArrayList<>();
		keywordsTEMP = nlpData.getLemmaArrayList();
		System.out.println(keywordsTEMP);
		return keywordsTEMP;
	}

	public static void main(String args[]) {
		KeywordAnalysis keywords = new KeywordAnalysis();
		keywords.nlpKeywordTagger();
	}
 }
