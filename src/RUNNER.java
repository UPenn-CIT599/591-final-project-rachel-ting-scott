import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
		urlTextBox = new JTextField(350);
		urlTextBox.setBounds(50,100,500,20);
		resultAnalysis = new JTextArea(10,30);
		resultAnalysis.setBounds(50,200,500,300);
		resultAnalysis.setEditable(false);
		OKButton = new JButton("OK");
		OKButton.setBounds(550,100,30,30);
		OKButton.addActionListener(this);
		
		frame.add(myLabel);
		frame.add(urlTextBox);
		frame.add(OKButton);
		frame.add(result);
		frame.add(resultAnalysis);
		frame.setSize(600, 600);
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
			for (Map.Entry<String, Long> lemmaMap : nlp.getTopLemmaToCountList()) {
				lemma += lemmaMap.getKey() + " ";
			}
			System.out.println(nlp.getTopLemmaToCountList());
			
			//3. print out the top names
			nlp.findPeople();
			nlp.findTopPeople(nlp.getPeopleInArticleArrayList());
			//List<Map.Entry<String, Long>> topPeopleToCountList
			String topPeople = "";
			for (Map.Entry<String, Long> peopleMap : nlp.getTopPeopleToCountList()) {
				topPeople += peopleMap.getKey() + " ";
			}
			
			//4. print out the recommended urls
			URLRecommender recommender = new URLRecommender();
			// change lemma to arrayList
			String str[] = lemma.split(" ");
			ArrayList<String> topKeywords = new ArrayList<String>();
			for (String singleStr : str) {
				topKeywords.add(singleStr);
			}
 			recommender.recommendURL(recommender.createMap(), topKeywords);
			;
			String ListOfRecommendedURL = "";
			for (String recommendedURL : recommender.getRecommendations()) {
				ListOfRecommendedURL += recommendedURL + "\n";
			}
			
			//5. print out the positivity score
			nlp.getPositivityScore(nlp.getTokenArrayList());
			int positivityScore =  nlp.getPositivityScore();
			
			
			
			resultAnalysis.setText(
					"The title is: " + title + "\n" +
					"The top keywords are: " + lemma + "\n" +
					"The top names are: " + topPeople + "\n" +
					"We recommend these websites to you: " + "\n" + ListOfRecommendedURL + "\n" +
					"The positivity score is: " + positivityScore);
		}
	}
	
	public static void main(String[] args) {
		new RUNNER();
	}
	
	
//	@Override
//	public void run() {
//	//create all the components	
//	JFrame frame = new JFrame("Website Analyzer");
//	JPanel outerPanel = new JPanel(new GridLayout(3,3));
//	JPanel innerTopPanel = new JPanel(new FlowLayout());
//	JLabel myLabel = new JLabel("Please enter a URL: (Please include http://)");
//	JPanel innerCenterPanel = new JPanel(new FlowLayout());
//	JTextField urlTextBox = new JTextField(30);
//	JButton OKButton = new JButton("OK");
//	JPanel innerBottomPanel = new JPanel(new FlowLayout());
//	JLabel result = new JLabel("Result");
//	JTextArea resultAnalysis = new JTextArea(10,30);
//	resultAnalysis.setEditable(false);
//	
//	//add the components together
//	//Frame > outerPanel > innerTopPanel     >  myLabel
//	//					   innerCenterPanel  >  urlTextBox  + OKButton
//	//					   innerBottomPanel  >  result + resultAnalysis
//	frame.add(outerPanel);
//	outerPanel.add(innerTopPanel);
//	innerTopPanel.add(myLabel);
//	innerTopPanel.add(innerCenterPanel);
//	innerCenterPanel.add(urlTextBox);
//	innerCenterPanel.add(OKButton);
//	outerPanel.add(innerBottomPanel);
//	innerBottomPanel.add(result);
//	innerBottomPanel.add(resultAnalysis);
//	
//	
//	//urlTextBox ActionListener
//	class urlListener implements ActionListener {		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			OKButton.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					String url = urlTextBox.getText();
//					if(e.getSource() == OKButton) {	
//						resultAnalysis.setText(url);
//				}	
//			}});
//			}
//		}
//		
//	
////	urlTextBox.addActionListener(new ActionListener() {
////		@Override
////		public void actionPerformed(ActionEvent e) {			
////			AbstractButton myJTextField = null;
////			String url = myJTextField.getText();
////		}	
////	});
//	
//	//OKButton ActionListener
////	OKButton.addActionListener(new ActionListener() {
////		@Override
////		public void actionPerformed(ActionEvent e) {
////			;
////		}	
////	});
//	
//		
//	//required pieces of code
//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	frame.pack();
//	frame.setVisible(true);	
//		
//	}
//	
//	public static void main(String[] args) {
////		NLPData nlp = new NLPData();
//		SwingUtilities.invokeLater(new RUNNER());
//	}

}
