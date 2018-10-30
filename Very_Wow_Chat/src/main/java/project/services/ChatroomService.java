package project.services;

import project.persistance.entities.Chatroom;
import project.persistance.entities.User;
import project.persistance.repositories.ChatroomRepository;
import project.persistance.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ChatroomService {
	// logs all neo4j calls
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private final ChatroomRepository chatroomRepository ;
	
	public ChatroomService(ChatroomRepository chatroomRepository) {
		this.chatroomRepository = chatroomRepository;
	}

	/**
	 * Check if a chatroom exists with a given chatroomName
	 * @param chatroomName	a user's userName
	 * @return true if chatroomName is in use, else false
	 */
	public Boolean chatroomExists(String chatroomName) {
		Chatroom chatroom = this.chatroomRepository.findByChatroomName(chatroomName);
		return chatroom != null;
	}
	
	/**
	 * create a chatroom
	 * @param newChatroom
	 * @return the newly created chatroom
	 */
	@Transactional(readOnly = false)
	public Chatroom createChatroom(User user, Chatroom newChatroom) {
		// throw error if username is taken
		if(chatroomExists(newChatroom.getChatroomName())) {
			throw new IllegalArgumentException("Chatoom name is already in use.");
		}
		Chatroom chatroom = chatroomRepository.save(newChatroom);
		
	
		return chatroom;
	}

	/**
	 * save a chatroom, used to apply updates
	 * @param chatroom
	 * @return the newly updates
	 */
	public Chatroom saveChatroom(Chatroom chatroom) {
		return chatroomRepository.save(chatroom);
	}
	
	/**
	 * delete a chatroom (and all its relations
	 * @param chatroom
	 */
	@Transactional(readOnly = false)
	public void deleteChatroom(Chatroom chatroom) {
		chatroomRepository.delete(chatroom);
	}
}
