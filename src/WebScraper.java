import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * Scrapes user-provided URL for relevant HTML elements (paragraph 
 * elements and header elements). It first cleans the HTML and only
 * keeps "safe" elements (including "p", "li", "h1", "h2", "h3", "h4", "h5",
 * "a", "href", "target"), then pulls relevant tags.
 * @author Rachel Friend, Ting-Hsuan Lee, Scott Theer
 */
public class WebScraper {
	
	public static String referrer = "http://www.google.com";
	public static String userAgent = "Mozilla";
	
	
	public WebScraper() {	
	}
	
	/**
	 * Cleans HTML to remove any elements that could be
	 * potentially dangerous to class (XSS attacks), or are not necessary
	 * for extraction
	 * @param content JSOUP HTML
	 * @param additionalTags Tags to be kept in HTML
	 * @return String cleaned HTML
	 */
	public String cleanContent(String content, ArrayList<String> additionalTags) {
		Whitelist cleanValues = Whitelist.simpleText();
		
		cleanValues.addTags("p", "li", "h1", "h2", "h3", "h4", "h5");
		if(additionalTags != null) {
			for(String tag: additionalTags) {
				cleanValues.addTags(tag);
			}
			if (additionalTags.contains("a")) {
				cleanValues.addAttributes("a", "href", "target"); 
			}
		}
		Document dirty = Jsoup.parseBodyFragment(content, "");
		Cleaner cleaner = new Cleaner(cleanValues);
		Document clean = cleaner.clean(dirty);
		String safe = clean.body().html();
		return safe;
	}
	
	/**
	 * Test URL connection and grab URL if connection is valid
	 * @param url User-provided URL
	 * @return String raw HTML
	 * @throws IllegalArgumentException
	 */
	public String getConnection(String url) throws IllegalArgumentException {
        Connection conn = Jsoup.connect(url).ignoreContentType(true).userAgent(userAgent).referrer(referrer).timeout(12000).followRedirects(true);
        String out;
        try {
            Response resp = conn.execute();
            if (resp.statusCode() != 200) {
                System.out.println("Error: " + resp.statusCode());
                out = "connection error";
            }else{	
                System.out.println("Downloading html from " + url);
                String html = conn.get().html();
                out = html;
            }   
        }catch(IOException e) {
             System.out.println(Thread.currentThread().getName() + " cannot connect to " + url + " " + e);
             System.out.println("Cannot connect!");
             out = "connection error";
        }
		return out;
	}
	
	/**
	 * Grab relevant HTML tags
	 * @param safeHtml cleaned HTML
	 * @return String relevant HTML tags
	 */
	public String readHtml(String safeHtml) {
		Document doc = Jsoup.parse(safeHtml);
		Elements paragraphs = doc.getElementsByTag("p");
		Elements headers = doc.select("h1, h2, h3, h4, h5");
		String title = headers.text();
		String paras = paragraphs.text();
		return title + " " + paras;
	}

	/**
	 * Run URL connection, clean HTML, and grab relevant tags
	 * @param url User-provided URL
	 * @return String final HTML
	 */
	public String runScraper(String url) {
		boolean done = true;
		String outParas = null;
		while(done) {
			try{
				if(!url.contains("http")) { 
					 url = "https://" + url;
				}
				String rawHtml = getConnection(url);
				String safeHtml = cleanContent(rawHtml, null);
				outParas = readHtml(safeHtml);
				System.out.println(outParas);
				done = false;
			}catch(IllegalArgumentException e) {
				System.out.println("Invalid URL!");
			}
		}
		return outParas;
    }
}
