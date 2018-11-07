package project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.Errors.NotFoundException;
import project.persistance.entities.ChatMessage;
import project.persistance.entities.Chatroom;
import project.services.ChatMessageTestService;
import project.services.ChatroomService;
import project.services.UserService;

@RestController
@RequestMapping("/chatroomtest")
public class ChatroomTestController extends ChatroomController {

	private final ChatMessageTestService chatMessageTestService;

	public ChatroomTestController(ChatroomService chatroomService, UserService userService,
			ChatMessageTestService chatMessageTestService) {
		super(chatroomService, userService);
		this.chatMessageTestService = chatMessageTestService;
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
		
		
		
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			
			// TODO: figure out which user has sent the request. 
			
			// TODO: ensure some that no some random user can get the chat log
			// of any chat room he or she desires.
			
			List<ChatMessage> chatMessages = chatMessageTestService.getChatPage(chatroom, limit, offset);
			
			
			// TODO: this is only temporary.  Figure out how to do this
			// correctly.
			
			List<ChatMessage> body = chatMessages;
			
			
			
			return new ResponseEntity<>(body, HttpStatus.OK);
			
		} catch (NotFoundException e) {
			// e.printStackTrace();
			return e.getErrorResponseEntity();
		}
	}

}
