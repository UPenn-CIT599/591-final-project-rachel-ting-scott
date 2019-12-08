import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * This class runs the output for the keyword extraction and sentiment analysis of a user's URL input
 * @author Rachel Friend, Scott Theer, Ting-Hsuan Lee
 *
 */
public class RUNNER implements ActionListener{
	//instance variables
	JLabel myLabel;
	JLabel result;
	JTextField urlTextBox;
	JTextArea resultAnalysis; 
	JButton OKButton;
	
	
	//constructor
	public RUNNER() {
	
		JFrame frame = new JFrame("Website Analyzer");
		myLabel = new JLabel("Please enter a URL: (Please include http://)");
		myLabel.setBounds(50,50,300,20);
		result = new JLabel("Website Analysis Result");
		result.setBounds(50,150,300,20);
		urlTextBox = new JTextField(800);
		urlTextBox.setBounds(50,100,800,20);
		resultAnalysis = new JTextArea(10,30);
		resultAnalysis.setBounds(50,200,800,300);
		resultAnalysis.setEditable(false);
		OKButton = new JButton("OK");
		OKButton.setBounds(850,100,30,30);
		OKButton.addActionListener(this);
		
		frame.add(myLabel);
		frame.add(urlTextBox);
		frame.add(OKButton);
		frame.add(result);
		frame.add(resultAnalysis);
		frame.setSize(1000, 600);
		frame.setLayout(null);
		frame.setVisible(true);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String url = urlTextBox.getText();
		
		if (e.getSource() == OKButton) {
			
			WebScraper scraper = new WebScraper();
			String toBeAnalysed = scraper.runScraper(url);
			NLPData nlp = new NLPData(toBeAnalysed);
			nlp.tokenizer(nlp.getWebPageText());
			ArrayList<String> temp = nlp.getTokenArrayList();
			nlp.lemmatizer(nlp.getTokenArrayList());
			
			//1. print out the headline
			String title = "";
			title = nlp.getTitleOrFirstSentence();
			
			//2. print out the top keywords
			nlp.findTopLemma(nlp.getLemmaArrayList());
			//List<Map.Entry<String, Long>> topLemmaToCountList
			String lemma = "";
			int i = 1;
			for (Map.Entry<String, Long> lemmaMap : nlp.getTopLemmaToCountList()) {
				lemma += i + "." + lemmaMap.getKey() + " ";
				i++;
			}
			
			//3. print out the top names
			nlp.findPeople(toBeAnalysed); // this doesn't work now!!
			nlp.findTopPeople(nlp.getPeopleInArticleArrayList());
			//List<Map.Entry<String, Long>> topPeopleToCountList
			String topPeople = "";
			int n = 1;
			for (Map.Entry<String, Long> peopleMap : nlp.getTopPeopleToCountList()) {
				topPeople += n + "." + peopleMap.getKey() + " ";
				n++;
			}
			
			//4. print out the recommended urls
			URLRecommender recommender = new URLRecommender();
			
			// change keywords to arrayList
			String keywords = "";
			for (Map.Entry<String, Long> lemmaMap : nlp.getTopLemmaToCountList()) {
				keywords += lemmaMap.getKey() + " ";
			}
			String str[] = keywords.split(" ");
			ArrayList<String> topKeywords = new ArrayList<String>();
			for (String singleStr : str) {
				topKeywords.add(singleStr);
			}
			
			//test
			System.out.println(topKeywords);
			
			HashMap<String[], String> hash = recommender.createMap();
			//test
			for (String[] list : hash.keySet()) {
				System.out.println(Arrays.toString(list));
			}
			
 			recommender.recommendURL(hash, topKeywords);
 			ArrayList<String> recommend = recommender.getRecommendations();
 			
 			//test
 			System.out.println(recommend);
 			
			String ListOfRecommendedURL = "";
			for (String recommendedURL : recommender.getRecommendations()) {
				ListOfRecommendedURL += recommendedURL + "\n";
			}
			
			//5. print out the positivity score
			nlp.getPositivityScore(nlp.getTokenArrayList());
			int positivityScore =  nlp.getPositivityScore();
			
			
			
			resultAnalysis.setText(
					"The title is: " + "\n" + title + "\n\n" +
					"The top keywords are: " + "\n" + lemma + "\n\n" +
					"The top names are: " + "\n" + topPeople + "\n\n" +
					"We recommend these websites to you: " + "\n" + ListOfRecommendedURL + "\n" +
					"The positivity score is: " + positivityScore);
		}
	}
	
	public static void main(String[] args) {
		new RUNNER();
	}

}
