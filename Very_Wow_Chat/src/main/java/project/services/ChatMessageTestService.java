package project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import project.persistance.entities.ChatMessage;
import project.persistance.entities.Chatroom;
import project.persistance.repositories.mongo.ChatMessageRepository;

/**
 * TODO: finish implementing.
 * 
 * TODO: this class should reference ChatMessageRepository.java.
 * 
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
@Service
public class ChatMessageTestService {
	
	// TODO: should this be autowired or?
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	



	/**
	 * TODO finish implementing
	 * 
	 * @param chatroom
	 * @param limit
	 * @param offset
	 * @return
	 */
	/*public List<ChatMessage> getChatPage(Chatroom chatroom, int limit, int offset) {
		long id = chatroom.getId();
		List<ChatMessage> chatMessages = chatMessageRepository.findPagedResultByChatroomName(id, limit, offset);
		return chatMessages;
	}*/
		
}
