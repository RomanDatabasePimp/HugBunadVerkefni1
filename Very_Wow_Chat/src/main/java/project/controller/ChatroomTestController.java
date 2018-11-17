package project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.Errors.NotFoundException;
import project.payloads.ChatMessageRequest;
import project.payloads.ResponseWrapper;
import project.persistance.entities.ChatMessage;
import project.persistance.entities.Chatroom;
import project.persistance.entities.User;
import project.services.ChatMessageTestService;

@RestController
@RequestMapping("/auth/chatroom2")
public class ChatroomTestController extends ChatroomController {

	@Autowired
	private ChatMessageTestService chatMessageTestService;


	/**
	 * Returns all messages of chat room `chatroomName`.
	 * 
	 * TODO: maybe later remove?
	 * 
	 * @param chatroomName Name of chat room.
	 * @return All messages of chat room.
	 */
	@RequestMapping(path = "/{chatroomName}/log", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatlogPage(@PathVariable String chatroomName, UsernamePasswordAuthenticationToken token) {

		// TODO: what if the chat room doesn't exist?
		
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			List<ChatMessage> chatMessages = chatMessageTestService.getAllMessages(chatroom);

			List<ChatMessage> body = chatMessages;

			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * Returns all messages of chat room ...
	 * 
	 * @param chatroomName
	 * @param limit
	 * @param offset
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/log/{offset}/{limit}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatlogPage(@PathVariable String chatroomName, @PathVariable int limit,
			@PathVariable int offset, UsernamePasswordAuthenticationToken token) {

		System.out.println("Get chat messages at offset and limited.");
		System.out.println("Not implemented!");

		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);

			List<ChatMessage> chatMessages = chatMessageTestService.getChatPage(chatroom, limit, offset);

			List<ChatMessage> body = chatMessages;

			return new ResponseEntity<>(body, HttpStatus.OK);

		} catch (NotFoundException e) {
			e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}
	
	/**
	 * 
	 * 
	 * @param chatroom
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/getmessagesfromtime/{startTime}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatroomMessagesFromStartTime(@PathVariable String chatroomName,
			@PathVariable Long startTime, @PathVariable Long endTime, UsernamePasswordAuthenticationToken token) {
		
		try {
			User user = userService.findByUsername(token.getName());
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			if (chatroomService.isMember(user, chatroom)) {
				List<ChatMessage> results = chatMessageTestService.getChatroomMessagesBetweenTime(chatroom, startTime, System.currentTimeMillis());
				System.out.println(results);
				return new ResponseEntity<>(results, HttpStatus.OK);
			} else {
				// TODO: add more descriptive error message.
				return new ResponseEntity<>("don't have access to this chat room", HttpStatus.OK);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}


	/**
	 * 
	 * 
	 * @param chatroom
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/getmessagesbetweentime/{startTime}/{endTime}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatroomMessagesBetweenTime(@PathVariable String chatroomName,
			@PathVariable Long startTime, @PathVariable Long endTime, UsernamePasswordAuthenticationToken token) {
		
		try {
			User user = userService.findByUsername(token.getName());
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			if (chatroomService.isMember(user, chatroom)) {
				List<ChatMessage> results = chatMessageTestService.getChatroomMessagesBetweenTime(chatroom, startTime, endTime);
				System.out.println(results);
				return new ResponseEntity<>(results, HttpStatus.OK);
			} else {
				// TODO: add more descriptive error message.
				return new ResponseEntity<>("don't have access to this chat room", HttpStatus.OK);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * 
	 * http://localhost:9090/auth/chatroom2/c2/addchatmessage
	 * 
	 * JSON body
	 * {
	 * 	"message": "Hello world!"
	 * }
	 * 
	 * @param chatroom
	 * @param chatMessage
	 * @return
	 * @throws NotFoundException 
	 */
	@RequestMapping(path = "/{chatroomName}/addchatmessage", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> addChatMessage(@PathVariable String chatroomName,
			@RequestBody ChatMessageRequest chatMessageRequest, UsernamePasswordAuthenticationToken token) {

		try {
			User user = userService.findByUsername(token.getName());
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			
			if (chatroomService.isMember(user, chatroom)) {
				
				long timestamp = System.currentTimeMillis();
				
				String chatroomMessage = chatMessageRequest.getMessage();
				
				
				ChatMessage chatMessage = new ChatMessage(null, chatroomName, user.getId(), user.getUsername(), user.getDisplayName(), chatroomMessage, timestamp);
				
				chatMessageTestService.addChatMessage(chatMessage);
				
				return new ResponseEntity<>(timestamp, HttpStatus.OK);
			} else {
				// TODO: return something more informative.
				return new ResponseEntity<>("error", HttpStatus.OK);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}
}
