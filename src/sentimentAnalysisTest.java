import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class sentimentAnalysisTest {

	@Test
	void testGetNegativeWordCount() {
		HashMap<String, Integer> trialMap = new HashMap<>();
		trialMap.put("abnormal", 1);
		trialMap.put("acclaim", 2);
		sentimentAnalysis sA = new sentimentAnalysis(trialMap);
		assertEquals(sA.getNegativeWordCount(),1);
	}

	@Test
	void testGetPositiveWordCount() {
		HashMap<String, Integer> trialMap = new HashMap<>();
		trialMap.put("abnormal", 1);
		trialMap.put("acclaim", 2);
		sentimentAnalysis sA = new sentimentAnalysis(trialMap);
		assertEquals(sA.getPositiveWordCount(),2);
	}

	@Test
	void testGetScoreOutput() {
		HashMap<String, Integer> trialMap = new HashMap<>();
		trialMap.put("abnormal", 1);
		trialMap.put("acclaim", 2);
		sentimentAnalysis sA = new sentimentAnalysis(trialMap);
		assertEquals(sA.getScoreOutput(),1);
	}

}
