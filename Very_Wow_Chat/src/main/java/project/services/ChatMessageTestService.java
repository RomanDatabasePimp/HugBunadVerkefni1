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
	private final ChatMessageRepository chatMessageRepository;
	
	
	public ChatMessageTestService(ChatMessageRepository chatMessageRepository) {
		this.chatMessageRepository = chatMessageRepository;
	}


	/**
	 * TODO finish implementing
	 * 
	 * @param chatroom
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<ChatMessage> getChatPage(Chatroom chatroom, int limit, int offset) {
		long id = chatroom.getId();
		List<ChatMessage> chatMessages = chatMessageRepository.findPagedResultByChatroomId(id, limit, offset);
		return chatMessages;
	}
	
	@Document(collection = "users")
	public class Tork {
		
		@Id 
		String id;
		
		private String name;
		
		private int age;
		
		

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Tork(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		
		
	}
	
	public void insertSomeRandomShit() {
		
		Tork t = new Tork("Da", 13);
			
		
		// chatMessageRepository.insert(t);
	}
}
