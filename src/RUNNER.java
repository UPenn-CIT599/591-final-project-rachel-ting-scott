import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * This class runs the output for the keyword extraction and sentiment analysis of a user's URL input
 * @author Rachel Friend, Scott Theer, Ting-Hsuan Lee
 *
 */
public class RUNNER implements Runnable{
	@Override
	public void run() {
	//create all the components	
	JFrame frame = new JFrame("Website Analyzer");
	JPanel outerPanel = new JPanel(new GridLayout(3,3));
	JPanel innerTopPanel = new JPanel(new FlowLayout());
	JLabel myLabel = new JLabel("Please enter a URL: (Please include http://)");
	JPanel innerCenterPanel = new JPanel(new FlowLayout());
	JTextArea urlTextBox = new JTextArea(1,30);
	JPanel innerBottomPanel = new JPanel(new GridLayout(1,1));
	JButton OKButton = new JButton("OK");
	
	//add the components together
	//Frame > outerPanel > innerTopPanel     >  myLabel
	//					   innerCenterPanel  >  urlTextBox
	//					   innerBottomPanel  >  OKButton
	frame.add(outerPanel);
	outerPanel.add(innerTopPanel);
	innerTopPanel.add(myLabel);
	innerTopPanel.add(innerCenterPanel);
	innerCenterPanel.add(urlTextBox);
	innerCenterPanel.add(innerBottomPanel);
	innerBottomPanel.add(OKButton);
		
		
	//required pieces of code
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);	
		
	}
	
	public static void main(String[] args) {
//		NLPData nlp = new NLPData();
		SwingUtilities.invokeLater(new RUNNER());
	}

}
