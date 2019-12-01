import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class WebScraper_copy_for_RUNNER {
	
	public static String referrer = "http://www.google.com";
	public static String userAgent = "Mozilla";
	private String url;
	
	public WebScraper_copy_for_RUNNER(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}



	public String cleanContent(String content, ArrayList<String> additionalTags) {
		Whitelist cleanValues = Whitelist.simpleText();
		cleanValues.addTags("p", "li");
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
	
	public String getConnection(String url) throws IllegalArgumentException {
        Connection conn = Jsoup.connect(url).ignoreContentType(true).userAgent(userAgent).referrer(referrer).timeout(12000).followRedirects(true);//5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
        String out;
        try {
            Response resp = conn.execute();
            if (resp.statusCode() != 200) {
                System.out.println("Error: " + resp.statusCode());
                out = "connection error";
            }else{	
                System.out.println("Downloading " + url + " html");
                String html = conn.get().html();
                out = html;
            }   
        }catch(IOException e) {
             System.out.println(Thread.currentThread().getName() + "Cannot connect to  " + url + e);
             System.out.println("Cannot connect!");
             out = "connection error";
        }
		return out;
	}
	
	public String readHtml(String safeHtml) {
		Document doc = Jsoup.parse(safeHtml);
		Elements paragraphs = doc.getElementsByTag("p");
		String paras = paragraphs.text();
		return paras;
	}

	public String runScraper() {
		
//		Scanner s = new Scanner(System.in);
//		System.out.println("Provide URL (with http prefix!): ");
		boolean done = true;
		String outParas = null;
		while(done) {
			try{
				String rawHtml = getConnection(url);
				String safeHtml = cleanContent(rawHtml, null);
				outParas = readHtml(safeHtml);
				System.out.println(outParas);
				done = false;
			}catch(IllegalArgumentException e) {
				System.out.println("Invalid URL!");
			}
		}
//		s.close();
		return outParas;
    }
	
	public static void main(String[] args) {
		WebScraper_copy_for_RUNNER ws = new WebScraper_copy_for_RUNNER("https://www.nytimes.com/2019/11/27/learning/lesson-of-the-day-the-hoodie-enters-the-museum.html");
		String out = ws.runScraper();
	}
}
