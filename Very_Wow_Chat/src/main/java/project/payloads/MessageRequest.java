package project.payloads;

import project.services.CryptographyService;

/**
 * 
 * NOTE: messages that are inserted into this container are assumed to
 * be in plaintext, and are encrypted so they eventually store ciphertext
 * messages.
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
public class MessageRequest {
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = CryptographyService.getCiphertext(message);
	}
	
	public MessageRequest() {}

	public MessageRequest(String message) {
		this.message = CryptographyService.getCiphertext(message);
	}
}
