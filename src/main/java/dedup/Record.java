package dedup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record {
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private String address;
	private LocalDateTime dateTime;
	
	
	public Record(String id, String email, String firstName, String lastName, String address, String dateTime) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		this.dateTime = LocalDateTime.parse(dateTime, formatter);
	}
	
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(LocalDateTime time) {
		this.dateTime = time;
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void printRecordFieldChange(Record r2) {
		if (!this.getId().equals(r2.getId())) System.out.println("- ID --> From: " + this.getId() + " To: " + r2.getId());
		if (!this.getEmail().equals(r2.getEmail())) System.out.println("- Email --> From: " + this.getEmail() + " To: " + r2.getEmail());
		if (!this.getFirstName().equals(r2.getFirstName())) System.out.println("- First Name --> From: " + this.getFirstName() + " To: " + r2.getFirstName());
		if (!this.getLastName().equals(r2.getLastName())) System.out.println("- Last Name --> From: " + this.getLastName() + " To: " + r2.getLastName());
		if (!this.getAddress().equals(r2.getAddress())) System.out.println("- Address --> From: " + this.getAddress() + " To: " + r2.getAddress());
		if (!this.getDateTime().equals(r2.getDateTime())) System.out.println("- Entry Date --> From: " + this.getDateTime() + " To: " + r2.getDateTime());
	}
}
