package dedup;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Deduplicate {
	// File path for json files
	final private static String inputFilePath = "./src/main/resources/input/";
	final private static String outputFilePath = "./src/main/resources/output/";
	final private static String testInputFilePath = "./src/test/resources/input/";
	final private static String testOutputFilePath = "./src/test/resources/output/";
	// Name of the JSON array in the input file to read from
	final private static String jsonArrayName = "leads";
	
	
	
	// Method to write the deduplicated list to an output file
	public static JSONObject dedupOutput(String fileName) {
		FileWriter outputFile = null;
		JSONArray jsonArray = new JSONArray();
		
		List<Record> recordList = dedup(fileName);
		
		for (Record record : recordList) {
			JSONObject recordObject = new JSONObject();
			recordObject.put("_id", record.getId());
			recordObject.put("email", record.getEmail());
			recordObject.put("firstName", record.getFirstName());
			recordObject.put("lastName", record.getLastName());
			recordObject.put("address", record.getAddress());
			recordObject.put("entryDate", record.getDateTime().toString());
			jsonArray.add(recordObject);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(jsonArrayName, jsonArray);
		
		// Write the deduplicated JSON Object to an output file
		try {
			outputFile = (fileName.indexOf("test") == 0) ? 
					new FileWriter(testOutputFilePath + fileName.substring(0, fileName.length()-5) + "Output.json") :
				new FileWriter(outputFilePath + fileName.substring(0, fileName.length()-5) + "Output.json");
			outputFile.write(jsonObject.toJSONString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputFile.flush();
				outputFile.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}
	
	
	
	// Deduplication method where filename is read in, turned into JSONObject then deduplication is stored in hashmaps.
	public static List<Record> dedup(String fileName) {
		HashMap<String, Record> ids = new HashMap<String, Record>();
		HashMap<String, Record> emails = new HashMap<String, Record>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = (fileName.indexOf("test") == 0) ? 
					parser.parse(new FileReader(testInputFilePath + fileName)) 
					: parser.parse(new FileReader(inputFilePath + fileName));
			
			JSONObject jsonObject = (JSONObject) obj;
			
			JSONArray jsonArray = (JSONArray) jsonObject.get(jsonArrayName);
			
			Iterator<JSONObject> iter = jsonArray.iterator();
			
			System.out.println("---------- Searching for duplicate IDs ----------");
			while (iter.hasNext()) {
				JSONObject entry = iter.next();
				Record record = new Record(
						(String) entry.get("_id"), 
						(String) entry.get("email"), 
						(String) entry.get("firstName"), 
						(String) entry.get("lastName"), 
						(String) entry.get("address"), 
						(String) entry.get("entryDate"));
				
				if (!ids.containsKey(record.getId())) {
					ids.put(record.getId(), record);
				}
				else if (ids.get(record.getId()).getDateTime().isBefore(record.getDateTime()) ||
						ids.get(record.getId()).getDateTime().isEqual(record.getDateTime())) {
					System.out.println("FOUND DUPLICATE ID with later or same Date: " + record.getId());
					System.out.println("Replacing the following fields:");
					ids.get(record.getId()).printRecordFieldChange(record);
					ids.put(record.getId(), record);
				}
			}

			System.out.println("---------- Searching for duplicate Emails ----------");
			for (Record record : ids.values()) {
				if (!emails.containsKey(record.getEmail())){
					emails.put(record.getEmail(), record);
				}
				else if (emails.get(record.getEmail()).getDateTime().isBefore(record.getDateTime()) || 
						emails.get(record.getEmail()).getDateTime().isEqual(record.getDateTime())) {
					System.out.println("FOUND DUPLICATE EMAIL with later or same Date: " + record.getEmail());
					System.out.println("Replacing the following fields:");
					emails.get(record.getEmail()).printRecordFieldChange(record);
					emails.put(record.getEmail(), record);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Record>(emails.values());
	}
}
