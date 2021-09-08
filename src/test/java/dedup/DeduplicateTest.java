package dedup;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class DeduplicateTest {
	private final String testFile = "testDedupFile.json";
	private final String testFile2 = "testDedupFile2.json";
	private final String testEmptyFile = "testEmptyFile.json";
	private final String jsonArrayName = "leads"; 
	private Deduplicate deduplicate = new Deduplicate();
	
	@Test
	public void testDedup() {
		System.out.println("Running Test Cases for dedup method");
		Assert.assertEquals(5, deduplicate.dedup(testFile).size());
		Assert.assertEquals(4, deduplicate.dedup(testFile2).size());
		Assert.assertEquals(0, deduplicate.dedup(testEmptyFile).size());
	}
	
	@Test
	public void testWriteDedup() {
		System.out.println("Running Test Cases for dedupOutput method");
		JSONArray testOutputArray =  (JSONArray) deduplicate.dedupOutput(testFile).get(jsonArrayName);
		JSONArray testOutputArray2 = (JSONArray) deduplicate.dedupOutput(testFile2).get(jsonArrayName);
		JSONArray testOutputArrayEmpty = (JSONArray) deduplicate.dedupOutput(testEmptyFile).get(jsonArrayName);
		
		Assert.assertEquals(5, testOutputArray.size());
		Assert.assertEquals(4, testOutputArray2.size());
		Assert.assertEquals(0, testOutputArrayEmpty.size());
	}
}
