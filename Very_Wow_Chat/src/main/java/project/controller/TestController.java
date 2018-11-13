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

// TODO: delete this later
@RestController
public class TestController {

	private final ChatMessageTestService chatMessageTestService;

	public TestController(ChatMessageTestService chatMessageTestService) {
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
	@RequestMapping(path = "lol", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Object> getChatlogPage() {
		System.out.println("lol");
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
