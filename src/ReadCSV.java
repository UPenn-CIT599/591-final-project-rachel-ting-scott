import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class ReadCSV {

	public HashMap<String[], String> read() throws IOException {
		Path p1 = Paths.get("nytimes.csv");
		List<String> lines = Files.readAllLines(p1);
		String lines1 = String.join(" ", lines);
		lines1 = lines1.replaceAll("'", "");
	    HashMap<String[],String> map = new HashMap<String[], String>();
	    while(lines1.length() != 0){
	    	String[] arr1;
	    	try {
	    		String requiredString = lines1.substring(lines1.indexOf("[")+1, lines1.indexOf("]"));
	    		arr1 = requiredString.split(", ");
	    	} catch (StringIndexOutOfBoundsException e) {
				continue;
			}
	    	if(lines1.contains("http")) {
	    		lines1 = lines1.substring(lines1.indexOf("http"));
	    		String link;
	    		try {
	    			link = lines1.substring(0, lines1.indexOf(" "));
	    			map.put(arr1, link);
	    			lines1 = lines1.substring(lines1.indexOf(" "));
	    			
	    		} catch (StringIndexOutOfBoundsException e){
	    			link = lines1.substring(0, lines1.length());
	    			map.put(arr1, link);
	    			break;
	    		}
			}
	    }
	    return map;
	}
	
}
