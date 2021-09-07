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
}
