package project.persistance.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * TODO: finish implementing...
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
// @Document(collection = "chatmessage")
public class ChatMessage {

	// MongoDB ID
	@Id
	private String id;

	
	private long chatroomId;
	private User sender;
	private String message;
	private long timestamp;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getChatroomId() {
		return chatroomId;
	}
	public void setChatroomId(long chatroomId) {
		this.chatroomId = chatroomId;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
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
	
	

}
