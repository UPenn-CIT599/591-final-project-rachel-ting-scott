import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WebScraperTest {

	@Test
	void testConnection() {
		WebScraper ws = new WebScraper();
		String outHtml;
		try{
			outHtml = ws.getConnection("https://towardsdatascience.com/industrial-classification-of-websites-by-machine-learning-with-hands-on-python-3761b1b530f1");
		}catch(IllegalArgumentException e) {
			System.out.println("Invalid URL!");
			outHtml = null;
		}
		assertEquals("<!doctype html>", outHtml.split("\n", 2)[0], "HTML not pulled correctly!");
	}
	
	@Test
	void testCleanHtml() {
		WebScraper ws = new WebScraper();
		String outHtml;
		try{
			outHtml = ws.getConnection("https://towardsdatascience.com/industrial-classification-of-websites-by-machine-learning-with-hands-on-python-3761b1b530f1");
		}catch(IllegalArgumentException e) {
			System.out.println("Invalid URL!");
			outHtml = null;
		}
		String safeHtml = ws.cleanContent(outHtml, null);
		boolean test;
		if(safeHtml.contains("span")){
			test = true;
		}else{
			test = false;
		}
		assertEquals(test, false, "Should not include span");
	}
}

