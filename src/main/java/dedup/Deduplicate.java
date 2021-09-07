package dedup;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Deduplicate {
	// File path for json files
	final private static String filePath = "./src/main/resources/";
	// Name of the JSON array in the input file to read from
	final private static String jsonArrayName = "leads";
	
	
	// Method to write the deduplicated list to an output file
	public static void writeToFile(ArrayList<Record> recordList, String fileName) {
		FileWriter outputFile = null;
		JSONArray jsonArray = new JSONArray();
		
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
		
		JSONObject leadsObject = new JSONObject();
		leadsObject.put(jsonArrayName, jsonArray);
		
		// Write the deduplicated JSON Object to an output file
		try {
			outputFile = new FileWriter(filePath + fileName.substring(0, fileName.length()-5) + "Output.json");
			outputFile.write(leadsObject.toJSONString());
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
	}
	
	// Method for only printing changes between two records when a Record needs to be replaced
	public static void printRecordFieldChange(Record r1, Record r2) {
		if (!r1.getId().equals(r2.getId())) System.out.println("- ID --> From: " + r1.getId() + " To: " + r2.getId());
		if (!r1.getEmail().equals(r2.getEmail())) System.out.println("- Email --> From: " + r1.getEmail() + " To: " + r2.getEmail());
		if (!r1.getFirstName().equals(r2.getFirstName())) System.out.println("- First Name --> From: " + r1.getFirstName() + " To: " + r2.getFirstName());
		if (!r1.getLastName().equals(r2.getLastName())) System.out.println("- Last Name --> From: " + r1.getLastName() + " To: " + r2.getLastName());
		if (!r1.getAddress().equals(r2.getAddress())) System.out.println("- Address --> From: " + r1.getAddress() + " To: " + r2.getAddress());
		if (!r1.getDateTime().equals(r2.getDateTime())) System.out.println("- Entry Date --> From: " + r1.getDateTime() + " To: " + r2.getDateTime());
	}
	
	
	// Deduplication method where filename is read in, turned into JSONObject then deduplication is stored in hashmaps.
	public static void dedup(String fileName) {
		HashMap<String, Record> ids = new HashMap<String, Record>();
		HashMap<String, Record> emails = new HashMap<String, Record>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(filePath + fileName));
			
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
					System.out.println("FOUND DUPLICATE ID with later Date: " + record.getId());
					System.out.println("Replacing the following fields:");
					printRecordFieldChange(ids.get(record.getId()), record);
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
					System.out.println("FOUND DUPLICATE EMAIL with later Date: " + record.getEmail());
					System.out.println("Replacing the following fields:");
					printRecordFieldChange(emails.get(record.getEmail()), record);
					emails.put(record.getEmail(), record);
				}
			}
			
			writeToFile(new ArrayList<Record>(emails.values()), fileName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String fileName = args[0];
		dedup(fileName);
	}
}
