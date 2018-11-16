package project.persistance.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * TODO: finish implementing...
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
@Document(collection = "chatMessage")
public class ChatMessage {

	// MongoDB ID
	@Id
	private String id;
	
	// Rel. to neo4j
	private long chatroomId;
	private String chatroomName;
	
	// Rel. to neo4j
	private long senderUsernameId;
	private String senderUsername;
	
	private String message;
	
	@Indexed
	private Long timestamp = System.currentTimeMillis();  // time since epoch (doesn't work :()

	

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


	public String getChatroomName() {
		return chatroomName;
	}


	public void setChatroomName(String chatroomName) {
		this.chatroomName = chatroomName;
	}


	public long getSenderUsernameId() {
		return senderUsernameId;
	}


	public void setSenderUsernameId(long senderUsernameId) {
		this.senderUsernameId = senderUsernameId;
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


	public Long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}


	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", chatroomId=" + chatroomId + ", chatroomName=" + chatroomName
				+ ", senderUsernameId=" + senderUsernameId + ", senderUsername=" + senderUsername + ", message="
				+ message + ", timestamp=" + timestamp + "]";
	}


	public ChatMessage(String id, long chatroomId, String chatroomName, long senderUsernameId, String senderUsername,
			String message, Long timestamp) {
		super();
		this.id = id;
		this.chatroomId = chatroomId;
		this.chatroomName = chatroomName;
		this.senderUsernameId = senderUsernameId;
		this.senderUsername = senderUsername;
		this.message = message;
		this.timestamp = timestamp;
	}

	
	
}
