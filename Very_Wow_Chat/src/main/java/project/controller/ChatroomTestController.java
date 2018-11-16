package project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.Errors.NotFoundException;
import project.persistance.entities.ChatMessage;
import project.persistance.entities.Chatroom;
import project.persistance.repositories.mongo.ChatMessageRepository;
import project.services.ChatMessageTestService;

@RestController
@RequestMapping("/auth/chatroom2")
public class ChatroomTestController extends ChatroomController {

	@Autowired
	private ChatMessageTestService chatMessageTestService;
	
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	

	@RequestMapping(path = "/{chatroomName}/postMessage", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> postMessage(@PathVariable String chatroomName, @RequestBody ChatMessage chatMessage) {
		
		
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			long chatroomId = chatroom.getId();
			
			chatMessage.setChatroomId(chatroomId);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// INTERCEPT MESSAGE MAH
		chatMessage.setTimestamp(System.currentTimeMillis());
		
		System.out.println("Chatroom name: " + chatroomName);
		System.out.println("Chat message: " + chatMessage);
		
		chatMessageRepository.postMessage(chatMessage);
		
		
		return null;
	}

	/**
	 * 
	 * 
	 * @param chatroomName
	 * @param limit
	 * @param offset
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/log/{offset}/{limit}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatlogPage(@PathVariable String chatroomName, @PathVariable int limit,
			@PathVariable int offset) {
		
	
		System.out.println("THIS IS EXECUTING...");
		
		
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			
			System.out.println(chatroom);

			// TODO: figure out which user has sent the request.

			// TODO: ensure some that no some random user can get the chat log
			// of any chat room he or she desires.

			List<ChatMessage> chatMessages = chatMessageTestService.getChatPage(chatroom, limit, offset);
			
			System.out.println(chatMessages);

			// TODO: this is only temporary. Figure out how to do this
			// correctly.

			List<ChatMessage> body = chatMessages;

			return new ResponseEntity<>(body, HttpStatus.OK);

		} catch (NotFoundException e) {
			// e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}

}
