package project.payloads;

public class ChatMessageRequest {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public ChatMessageRequest() {
		super();
	}

	public ChatMessageRequest(String message) {
		this.message = message;
	}
}
