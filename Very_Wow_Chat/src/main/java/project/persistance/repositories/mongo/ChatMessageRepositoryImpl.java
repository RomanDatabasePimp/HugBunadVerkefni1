package project.persistance.repositories.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import project.persistance.entities.ChatMessage;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
	
	@Autowired
	private MongoTemplate mongoTemplate;	
	

	/**
	 * 
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
	 * 
	 */
	@Override
	public List<ChatMessage> findPagedResultByChatroomName(String chatroomName, int offset) {
		
		Criteria criteria = Criteria.where("chatroomName").is(chatroomName);
		Query query = new Query(criteria);
		query.skip(offset);
		
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		
		return results;
	}

	@Override
	public void postMessage(ChatMessage message) {
		mongoTemplate.insert(message);	
	}

	@Override
	public List<ChatMessage> getAllMessages(String chatroomName) {
		
		// db.chatMessage.find({"chatroomName": "c2"} 
		Criteria criteria = Criteria.where("chatroomName").is(chatroomName);
		Query query = new Query(criteria);
		
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		
		return results;
	}

	@Override
	public void deleteAllChatMessagesOfChatroom(String chatroomName) {
		Criteria criteria = Criteria.where("chatroomName").is(chatroomName);
		Query query = new Query(criteria);
		mongoTemplate.findAllAndRemove(query, ChatMessage.class);
	}

	@Override
	public List<ChatMessage> getChatroomMessagesBetweenTime(String chatroomName, long startTime, long endTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chatroomName").is(chatroomName));
		query.addCriteria(Criteria.where("timestamp").gte(startTime).lte(endTime));
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		return results;
	}

	@Override
	public void addChatMessage(ChatMessage message) {
		mongoTemplate.insert(message);
	}

	@Override
	public long getNrOfMessage(String chatroomName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chatroomName").is(chatroomName));
		long count = mongoTemplate.count(query, ChatMessage.class);
		return count;
	}

}
