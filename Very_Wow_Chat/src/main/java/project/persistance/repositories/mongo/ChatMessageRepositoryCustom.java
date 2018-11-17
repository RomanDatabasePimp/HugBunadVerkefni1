package project.persistance.repositories.mongo;

import java.util.List;

import project.persistance.entities.ChatMessage;

public interface ChatMessageRepositoryCustom {
	/**
	 * Returns a paged list of chat messages.
	 *
	 * If M[1..n] was a list of all messages for a chat room C, then this
	 * method would return M[n - offset - limit, n - offset].
	 * 
	 * NOTE: Spring is 'tarded.  If you rename 
	 * 
	 * @param id CHATROOM ID
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<ChatMessage> findPagedResultByChatroomId(long id, int limit, int offset);
	
	
	List<ChatMessage> getAllMessages(String chatroomName);
	
	// void postMessage()
	
	void postMessage(ChatMessage message);
	
	void deleteAllChatMessagesOfChatroom(String chatroomName);
	
	List<ChatMessage> getChatroomMessagesBetweenTime(String chatroomName, long startTime, long endTime);
	
	void addChatMessage(ChatMessage message);
	
}
