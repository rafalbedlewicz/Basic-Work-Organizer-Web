
public class ResponseError {

	private String message;
	
	public ResponseError() {
		this.message = "";
	}
	
	public ResponseError(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	} 
	
	public String toString() {
		return message;
	}
	
	public boolean equals(String value) {
		return this.message.toString() == value.toString();
	}
}
