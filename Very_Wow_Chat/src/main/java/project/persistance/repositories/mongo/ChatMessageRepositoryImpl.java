package project.persistance.repositories.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import project.persistance.entities.ChatMessage;

/**
 * Implementation of custom methods for message repository.
 */
@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;	

	/**
	 * Fetches up to <code>limit</code> messages for chat room with chat room 
	 * name <code>chatroomName</code> starting from position <code>offset</code>.
	 * 
	 * @param chatroomName Name of chat room.
	 * @param offset The position where to fetch messages.
	 * @param limit Up to how many messages to fetch.
	 * 
	 * @return A list of chat messages.
	 */
	@Override
	public List<ChatMessage> findPagedResultByChatroomName(String chatroomName, int offset, int limit) {
		Criteria criteria = Criteria.where("chatroomName").is(chatroomName);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		return results;
	}
	
	/**
	 * Fetches all messages for chat room with name <code>chatroomName</code>
	 * starting from position <code>offset</code>.
	 * 
	 * @param chatroomName Name of chat room.
	 * @param offset Position of where to fetch messages.
	 * 
	 * @return List of chat messages.
	 */
	@Override
	public List<ChatMessage> findPagedResultByChatroomName(String chatroomName, int offset) {
		Criteria criteria = Criteria.where("chatroomName").is(chatroomName);
		Query query = new Query(criteria);
		query.skip(offset);
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		return results;
	}

	/**
	 * 
	 * @param chatroomName Name of chat room.
	 */
	@Override
	public void postMessage(ChatMessage message) {
		mongoTemplate.insert(message);	
	}

	/**
	 * 
	 * @param chatroomName Name of chat room. 
	 */
	@Override
	public List<ChatMessage> getAllMessages(String chatroomName) { 
		Criteria criteria = Criteria.where("chatroomName").is(chatroomName);
		Query query = new Query(criteria);
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		return results;
	}

	/**
	 * Deletes all messages that belong to chat room with name 
	 * <code>chatroomName</code>.
	 * 
	 * @param chatroomName Name of chat room.
	 */
	@Override
	public void deleteAllChatMessagesOfChatroom(String chatroomName) {
		Criteria criteria = Criteria.where("chatroomName").is(chatroomName);
		Query query = new Query(criteria);
		mongoTemplate.findAllAndRemove(query, ChatMessage.class);
	}

	/**
	 * 
	 * 
	 * @param chatroomName Name of chat room.
	 * @param startTime Unix time in milliseconds.  When to pick messages from.
	 * @param endTime Unix time in milliseconds.  When to pick messages to.
	 * 
	 * @return List of chat messages.
	 */
	@Override
	public List<ChatMessage> getChatroomMessagesBetweenTime(String chatroomName, long startTime, long endTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chatroomName").is(chatroomName));
		query.addCriteria(Criteria.where("timestamp").gte(startTime).lte(endTime));
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		return results;
	}

	/**
	 * Adds chat messages to database.  <code>message</code> contains which
	 * chat room it belongs to.
	 * 
	 * @param messages chat message container
	 */
	@Override
	public void addChatMessage(ChatMessage message) {
		mongoTemplate.insert(message);
	}

	/**
	 * Returns the number of messages in chat room with name <code>chatroomName</code>.
	 * 
	 * @param chatroomName Name of chat room.
	 * 
	 * @return How many messages are in chat room with name <code>chatroomName</code>.
	 */
	@Override
	public long getNrOfMessage(String chatroomName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chatroomName").is(chatroomName));
		long count = mongoTemplate.count(query, ChatMessage.class);
		return count;
	}

}
