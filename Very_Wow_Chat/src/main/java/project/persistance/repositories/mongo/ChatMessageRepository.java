package project.persistance.repositories.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import project.persistance.entities.ChatMessage;

/**
 * An interface for getting chat messages.
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageRepositoryCustom {
	
	/**
	 * Returns a list of chat messages 
	 * 
	 * TODO: doesn't this require some implementation?
	 *  
	 * 
	 * If M[1..n] was a list of all messages for a chat room C, then this
	 * method would return M[n - offset - limit, n - offset].
	 * 
	 * @param id The ID of the chat room.
	 * @param limit How many messages to get.
	 * @param offset Where offset index from the tail of the chat messages.
	 * @return
	 */
	List<ChatMessage> findPagedResultByChatroomName(long id, int limit, int offset);	
}
