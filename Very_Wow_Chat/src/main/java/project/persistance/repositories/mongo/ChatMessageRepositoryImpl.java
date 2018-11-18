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
	public List<ChatMessage> findPagedResultByChatroomId(long id, int limit, int offset) {
		
		Criteria criteria = Criteria.where("chatroomId").is(id);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		
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
		// TODO: test
		//  db.chatMessage.find({chatroomName: "c6", timestamp: { $gte: 1542462067973, $lte: 1542462082416 }}) 
		
		Criteria criteria = Criteria.where("chatroomName").is("c2");
		
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query);
	}

	@Override
	public List<ChatMessage> getChatroomMessagesBetweenTime(String chatroomName, long startTime, long endTime) {
		// db.chatMessage.find({timestamp: { $gte: 1542458691498, $lt: 1542458702913 }})
		
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
