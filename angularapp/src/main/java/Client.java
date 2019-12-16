import java.nio.charset.StandardCharsets;

import com.mysql.jdbc.Blob;

public class Client {
	
	private int id;
	private String name;
	private String lastname;
	private String phone;
	private String more;
	private boolean unwanted;
	
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
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getMore() {
		return more;
	}
	
	public void setMore(String more) {
		this.more = more;
	}
	
	public boolean getUnwanted() {
		return unwanted;
	}
	
	public void setUnwanted(boolean unwanted) {
		this.unwanted = unwanted;
	}
	
	
	public String toString() {
		return name + " " + lastname + " " + phone + " " + more + " " + unwanted;
	}
	
}