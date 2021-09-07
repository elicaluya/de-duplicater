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
	final private static String filePath = "./src/main/resources/";
	final private static String jsonArrayName = "leads";
	
	
	public static void writeToFile(ArrayList<Record> recordList, String fileName) {
		FileWriter outputFile = null;
		JSONArray leads = new JSONArray();
		
		for (Record record : recordList) {
			JSONObject recordObject = new JSONObject();
			recordObject.put("_id", record.getId());
			recordObject.put("email", record.getEmail());
			recordObject.put("firstName", record.getFirstName());
			recordObject.put("lastName", record.getLastName());
			recordObject.put("address", record.getAddress());
			recordObject.put("entryDate", record.getTime().toString());
			leads.add(recordObject);
		}
		
		JSONObject leadsObject = new JSONObject();
		leadsObject.put("leads", leads);
		
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
	
	
	public static void dedup(String fileName) {
		HashMap<String, Record> ids = new HashMap<String, Record>();
		HashMap<String, Record> emails = new HashMap<String, Record>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(filePath + fileName));
			
			JSONObject jsonObject = (JSONObject) obj;
			
			JSONArray jsonArray = (JSONArray) jsonObject.get(jsonArrayName);
			
			Iterator<JSONObject> iter = jsonArray.iterator();
			
			while (iter.hasNext()) {
				JSONObject entry = iter.next();
				Record record = new Record((String) entry.get("_id"), (String) entry.get("email"), (String) entry.get("firstName"), 
						(String) entry.get("lastName"), (String) entry.get("address"), (String) entry.get("entryDate"));
				
				if (!ids.containsKey(record.getId()) || 
						ids.get(record.getId()).getTime().isBefore(record.getTime()) ||
						ids.get(record.getId()).getTime().isEqual(record.getTime())) {
					ids.put(record.getId(), record);
				}
			}
			
			for (Record r : ids.values()) {
				if (!emails.containsKey(r.getEmail()) || 
						emails.get(r.getEmail()).getTime().isBefore(r.getTime()) || 
						emails.get(r.getEmail()).getTime().isEqual(r.getTime())) {
					emails.put(r.getEmail(), r);
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
