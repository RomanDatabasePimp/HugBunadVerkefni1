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
import project.services.MessageService;
import project.services.ChatroomService;
import project.services.UserService;

/**
 * Message controller
 * 
 * NOTE: it's always OK to use this methods even though the user is not a 
 * member of a chat room.
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
@RestController
@RequestMapping("/auth/chatroom")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private ChatroomService chatroomService;

	@Autowired
	private UserService userService;

	/**
	 * Returns all messages of chat room `chatroomName`.
	 * 
	 * @param chatroomName Name of chat room.
	 * @param token User name & password authentication token.
	 * 
	 * @return All messages of chat room.
	 */
	@RequestMapping(path = "/{chatroomName}/messages/all", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatlogPage(@PathVariable String chatroomName,
			UsernamePasswordAuthenticationToken token) {

		// TODO: what if the chat room doesn't exist?

		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			List<ChatMessage> chatMessages = messageService.getAllMessages(chatroom);

			List<ChatMessage> body = chatMessages;

			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}

	/** 
	 * Returns `limit` messages from chat room `chatroomName` starting from 
	 * `offset`.
	 * 
	 * @param chatroomName Name of chat room.
	 * @param limit How many messages at most to retrieve.
	 * @param offset Where to start retrieving messages.
	 * @param token User name & password authentication token.
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/messages/{offset}/{limit}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatlogPage(@PathVariable String chatroomName, @PathVariable int limit,
			@PathVariable int offset, UsernamePasswordAuthenticationToken token) {

		System.out.println("Get chat messages at offset and limited.");
		System.out.println("Not implemented!");

		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);

			List<ChatMessage> chatMessages = messageService.getChatPage(chatroom, limit, offset);

			List<ChatMessage> body = chatMessages;

			return new ResponseEntity<>(body, HttpStatus.OK);

		} catch (NotFoundException e) {
			e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * Returns all message from chat room `chatroomName` from time `startTime`
	 * until now.
	 * 
	 * @param chatroomName Name of chat room.
	 * @param startTime Start Unix time in milliseconds.
	 * @param token User name & password authentication token. 
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/messages/time/{startTime}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatroomMessagesFromStartTime(@PathVariable String chatroomName,
			@PathVariable Long startTime, @PathVariable Long endTime, UsernamePasswordAuthenticationToken token) {

		try {
			User user = userService.findByUsername(token.getName());
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			if (chatroomService.isMember(user, chatroom)) {
				List<ChatMessage> results = messageService.getChatroomMessagesBetweenTime(chatroom, startTime,
						System.currentTimeMillis());
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
	 * Returns all messages from chat room `chatroomName` starting from time
	 * `startTime` until time `endTime`.
	 * 
	 * @param chatroomName Name of chat room.
	 * @param startTime Start Unix time in milliseconds.
	 * @param endTime End Unix time in milliseconds.
	 * @param token User name & password authentication token.
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/messages/time/{startTime}/{endTime}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatroomMessagesBetweenTime(@PathVariable String chatroomName,
			@PathVariable Long startTime, @PathVariable Long endTime, UsernamePasswordAuthenticationToken token) {

		try {
			User user = userService.findByUsername(token.getName());
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			if (chatroomService.isMember(user, chatroom)) {
				List<ChatMessage> results = messageService.getChatroomMessagesBetweenTime(chatroom, startTime,
						endTime);
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
	 * Sends a message at chat room `chatroomName`.
	 * 
	 * The format of the JSON message should be like,
	 * 
	 * <pre>{ "message": "Hello world!" }</pre>
	 * 
	 * @param chatroomName Name of chat room.
	 * @param chatMessageRequest The message that is being sent.
	 * @param token User name & password authentication token.
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(path = "/{chatroomName}/message", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> addChatMessage(@PathVariable String chatroomName,
			@RequestBody ChatMessageRequest chatMessageRequest, UsernamePasswordAuthenticationToken token) {

		try {
			User user = userService.findByUsername(token.getName());
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);

			if (chatroomService.isMember(user, chatroom)) {

				long timestamp = System.currentTimeMillis();

				String chatroomMessage = chatMessageRequest.getMessage();

				ChatMessage chatMessage = new ChatMessage(null, chatroomName, user.getId(), user.getUsername(),
						user.getDisplayName(), chatroomMessage, timestamp);

				messageService.addChatMessage(chatMessage);

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
