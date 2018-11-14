package project.persistance.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * TODO: finish implementing...
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
@Document(collection = "chatmessage")
public class ChatMessage {

	// MongoDB ID
	@Id
	private String id;

	private String chatroomName;
	private String senderUsername;
	private String message;
	private long timestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChatroomName() {
		return chatroomName;
	}

	public void setChatroomName(String chatroomName) {
		this.chatroomName = chatroomName;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", chatroomName=" + chatroomName + ", senderUsername=" + senderUsername
				+ ", message=" + message + ", timestamp=" + timestamp + "]";
	}

	public ChatMessage(String id, String chatroomName, String senderUsername, String message, long timestamp) {
		super();
		this.id = id;
		this.chatroomName = chatroomName;
		this.senderUsername = senderUsername;
		this.message = message;
		this.timestamp = timestamp;
	}

}
