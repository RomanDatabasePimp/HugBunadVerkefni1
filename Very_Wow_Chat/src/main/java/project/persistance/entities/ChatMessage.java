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
	protected String id;
	
	// Rel. to neo4j
	protected String chatroomName;
	
	// Rel. to neo4j
	protected long senderUsernameId;
	protected String senderUsername;
	protected String senderdsplayName;
	
	private String message;
	
	@Indexed
	private Long timestamp;

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

	public String getSenderdsplayName() {
		return senderdsplayName;
	}

	public void setSenderdsplayName(String senderdsplayName) {
		this.senderdsplayName = senderdsplayName;
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
		return "ChatMessage [id=" + id + ", chatroomName=" + chatroomName + ", senderUsernameId=" + senderUsernameId
				+ ", senderUsername=" + senderUsername + ", senderdsplayName=" + senderdsplayName + ", message="
				+ message + ", timestamp=" + timestamp + "]";
	}

	public ChatMessage(String id, String chatroomName, long senderUsernameId, String senderUsername,
			String senderdsplayName, String message, Long timestamp) {
		super();
		this.id = id;
		this.chatroomName = chatroomName;
		this.senderUsernameId = senderUsernameId;
		this.senderUsername = senderUsername;
		this.senderdsplayName = senderdsplayName;
		this.message = message;
		this.timestamp = timestamp;
	}
	

	
	
	
	
}
