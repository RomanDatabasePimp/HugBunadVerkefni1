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
	

	@Override
	public List<ChatMessage> findPagedResultByChatroomId(long id, int limit, int offset) {
		
		System.out.println("GREAT!: findPagedResultByChatroomId");
		
		System.out.println("Chatroom ID: " + id);
		System.out.println("Limit: " + limit);
		System.out.println("Offset: " + offset);
		
		
		Criteria criteria = Criteria.where("chatroomId").is(id);
		Query query = new Query(criteria);
		// query.skip(offset);
		// query.limit(limit);
		
		List<ChatMessage> results = mongoTemplate.find(query, ChatMessage.class);
		
		System.out.println(results);
		
		// TODO Auto-generated method stub
		return results;
	}


	@Override
	public void postMessage(ChatMessage message) {
		// TODO Auto-generated method stub
		
		mongoTemplate.insert(message);
		
	}

}
