import java.nio.charset.StandardCharsets;

import com.mysql.jdbc.Blob;

public class Employee {
	
	private int id;
	private String name;
	private String lastname;
	private String photo;
	private String phone;
	private String color;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public void setPhoto(byte[] photo) {
		this.photo = new String(photo);
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String toString() {
		return name + " " + lastname + " " + photo + " " + phone + " " + color;
	}
	
}