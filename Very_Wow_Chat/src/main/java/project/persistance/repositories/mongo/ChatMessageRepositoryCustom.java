package project.persistance.repositories.mongo;

import java.util.List;

import project.persistance.entities.ChatMessage;

public interface ChatMessageRepositoryCustom {
	
	/**
	 * Returns a paged list of chat messages.
	 *
	 * If M[1..n] was a list of all messages for a chat room C, then this method
	 * would return M[n - offset - limit, n - offset].
	 * 
	 * @param id     CHATROOM ID
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<ChatMessage> findPagedResultByChatroomName(String chatroomName, int offset, int limit);

	/**
	 * 
	 * @param chatroomName
	 * @param offset
	 * @return
	 */
	List<ChatMessage> findPagedResultByChatroomName(String chatroomName, int offset);

	/**
	 * 
	 * @param chatroomName
	 * @return
	 */
	List<ChatMessage> getAllMessages(String chatroomName);

	/**
	 * 
	 * @param message
	 */
	void postMessage(ChatMessage message);

	/**
	 * 
	 * @param chatroomName
	 */
	void deleteAllChatMessagesOfChatroom(String chatroomName);

	/**
	 * 
	 * @param chatroomName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<ChatMessage> getChatroomMessagesBetweenTime(String chatroomName, long startTime, long endTime);

	/**
	 * 
	 * @param message
	 */
	void addChatMessage(ChatMessage message);

	/**
	 * 
	 * @return
	 */
	long getNrOfMessage(String chatroomName);

}
