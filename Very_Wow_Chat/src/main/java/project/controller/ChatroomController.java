package project.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import project.Errors.UnauthorizedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.persistance.entities.ErrorResponder;
import project.persistance.entities.User;
import project.persistance.entities.Chatroom;
import project.persistance.entities.UserResponder;
import project.persistance.entities.ChatroomResponder;
import project.services.ChatroomService;
import project.services.UserService;


/**
 * 
 * @author Vilhelml
 *
 */
@RestController
@RequestMapping("/chatroom")
public class ChatroomController {

	private final ChatroomService chatroomService;
	private final UserService userService;
	
	public ChatroomController(ChatroomService chatroomService, UserService userService) {
		this.chatroomService = chatroomService;
		this.userService = userService;
	}

	/**
	 * @param chatroomName: chatroomName of the chatroom to be returned
	 * @return: if found, return the chatroom with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/{chatroomName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getChatroom(@PathVariable String chatroomName){
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// wrap the data to send in json format
			ChatroomResponder body = new ChatroomResponder(chatroom);
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) { // user was not found
			// use wrapper to return error
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	/**
	 * @param username: chatroomName of the chatroom to be returned
	 * @return: if chatroom not found: return 404 not found
	 * 			if user is not the owner: return 401 unauthorized
	 * 			if successful: return 204 no content
	 */
	@RequestMapping(path = "/{chatroomName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<Object> deleteChatroom(@PathVariable String chatroomName){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		try {
			User user = userService.findByUsername("username1"); // get from token
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			if(chatroomService.isOwner(user, chatroom)) {
				ErrorResponder body = new ErrorResponder();
				body.setError("User is not the chatroom's owner");
				return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
			}
			// wrap the data to send in json format
			
			// DELETE ALL CHAT MESSAGES BEFORE DELETING THE CHATROOM!!!!
			
			chatroomService.deleteChatroom(chatroom);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(NoSuchElementException e) { // user was not found
			// use wrapper to return error
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * 
	 * @param newChatroom, a wrapper for the chatroom data
	 * @return the chatroom that was created, or an error message
	 */
	@RequestMapping(path = "/", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> createChatroom(@RequestBody ChatroomResponder newChatroom){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		try {
			User user = userService.findByUsername("username1"); // get from token
			// create chatroom from the payload
			Chatroom chatroom = new Chatroom(
					newChatroom.getChatroomName(),
					newChatroom.getDisplayName(),
					newChatroom.getDescription(),
					newChatroom.getListed(),
					newChatroom.getInvited_only()
			);
			// create the chatroom
			Chatroom result = chatroomService.createChatroom(user, chatroom);
			// wrap the chatroom data
			ChatroomResponder body = new ChatroomResponder(result);
			// return the chatroom and a 201 status code
			return new ResponseEntity<>(body, HttpStatus.CREATED);
		}catch(IllegalArgumentException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Send an invite from chatroom to user
	 * @param chatroomName: name of the chatroom to send the request
	 * @return: if found, return the chatroom with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/{chatroomName}/invite/{username}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> sendMemberInvitation(@PathVariable String chatroomName, @PathVariable String username){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		try {
			// the user sending the invite (the invitation will be sent by the chatroom, though)
			User user = userService.findByUsername("username1"); // get from token
			// the chatroom that the invite is for
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// the user receiving the invite
			User invitee = userService.findByUsername(username);
			// if the invitor doesn't have invite privilages
			if(!chatroomService.hasMemberInvitePrivilages(user, chatroom)) {
				ErrorResponder body = new ErrorResponder();
				body.setError("You do not have permission to invite users to this chatroom.");
				return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
			}
			// send the invite
			chatroomService.sendMemberInvite(invitee, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(NoSuchElementException e) { // user was not found
			// use wrapper to return error
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}catch(IllegalArgumentException e) {
			// use wrapper to return error
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Join an open chatrom or accept n invite
	 * @param chatroomName: name of the chatroom to be joined
	 * @return: if successful return a status code of 204, else error message with status code of 404 for not found, or 401 for unauthorized
	 */
	@RequestMapping(path = "/{chatroomName}/join", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> joinChatroom(@PathVariable String chatroomName){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		try {
			// the user sending the invite (the invitation will be sent by the chatroom, though)
			User user = userService.findByUsername("username2"); // get from token
			// the chatroom that the user wants to join
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// send the invite
			chatroomService.joinChatroom(user, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(IllegalArgumentException e) { // chatroom or user not found
			// use wrapper to return error
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}catch(UnauthorizedException e) { // no permission to perform action
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	// get all chatrooms
	
	// get listed chatrooms
	
	// send admin invite
	
	// accept admin invite
	
	// update chatroom
	
	// add/replace tags (?)
	// search for chatrroms with a tag
	
	// ban user, unban user (need to add banned user list)
	//	a used banned from a chatroom cannot request to join it, cannot receive invites to it
	
	// -------------------------- get requests for the chatroom's various lists -------------------------

	@RequestMapping(path = "/{chatroomName}/members", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getMembers(@PathVariable String chatroomName){
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			List<User> users = chatroom.getMembers();
			
			// create a list of ChatroomResponders for json return
			List<UserResponder> body = users.stream().map(x -> new UserResponder(x)).collect(Collectors.toList());
			
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/{chatroomName}/memberinvitees", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getMemberInvitees(@PathVariable String chatroomName){
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			List<User> users = chatroom.getMemberInvitees();
			
			// create a list of ChatroomResponders for json return
			List<UserResponder> body = users.stream().map(x -> new UserResponder(x)).collect(Collectors.toList());
			
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/{chatroomName}/administrators", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getAdministrators(@PathVariable String chatroomName){
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			List<User> users = chatroom.getAdministrators();
			
			// create a list of ChatroomResponders for json return
			List<UserResponder> body = users.stream().map(x -> new UserResponder(x)).collect(Collectors.toList());
			
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/{chatroomName}/admininvitees", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getAdminInvitees(@PathVariable String chatroomName){
		try {
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			List<User> users = chatroom.getAdminInvitees();
			
			// create a list of ChatroomResponders for json return
			List<UserResponder> body = users.stream().map(x -> new UserResponder(x)).collect(Collectors.toList());
			
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
}
