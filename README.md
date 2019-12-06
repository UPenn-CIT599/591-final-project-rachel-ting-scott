# final-project-rachel-ting-scott
final-project-rachel-ting-scott created by GitHub Classroom

Name:
News Article Recommender 

Description:
This program asks user's to input the URL of a news article and then analyzes the language of it to determine the top n content words 
and the top n people mentioned, provides a summary of the article (not yet - coming in the future), calculates a sentiment score based 
on positive or negative scoring on a scale, and provides recommendations for other articles that contain keywords that match the 
top n content words of the user's original web page. The recommended articles are from a Kaggle data set of NY Times articles: 
https://www.kaggle.com/harishcscode/all-news-articles-from-home-page-media-house/version/1#nytimes.csv

Instructions: 
Run the RUNNER class. In the pop-up window, enter the url of the website you wish to run the analysis and click the "OK" button. 
In the text box below, you will see the analysis results which include the title, the top keywords, the top names, some other websites 
recommended to you, and a score showing how positive this website is.

Installation:
To use this program, users should not need to install anything because all the .jar files are added here in the lib folder.
However, if installation is necessary, Mac users can follow these instructions to install via terminal: 
https://brewinstall.org/install-apache-opennlp-on-mac-with-brew/
or all users can download directly from here: https://opennlp.apache.org/download.html

Usage: 
For analysis of the language in news article and recommendations of other articles with similar content

Citations:
Open Source API used for natural language processing of text: https://opennlp.apache.org/;
List of stop words was created by combining different open source lists: 
https://gist.github.com/sebleier/554280 which is mostly based on the Natural Language Took Kit (NLTK: https://www.nltk.org/);
We added additional stop words from our own lists, and we also added punctuation to the stop words list to ensure cleaner 
lists of tokens, stems, and lemmas; 
Tutorial used for learning how to use OpenNLP: https://www.tutorialkart.com/opennlp/apache-opennlp-tutorial/;

Project Status: On-going. We are working on including a summary analysis and using an API for the sentiment analysis.
