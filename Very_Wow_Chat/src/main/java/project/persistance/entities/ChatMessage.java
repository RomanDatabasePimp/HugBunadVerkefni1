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
	
	
}
