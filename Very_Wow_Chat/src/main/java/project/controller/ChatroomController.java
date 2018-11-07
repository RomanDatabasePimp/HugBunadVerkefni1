package project.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import project.Errors.UnauthorizedException;
import project.Errors.BadRequestException;
import project.Errors.NotFoundException;
import project.Errors.HttpException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.persistance.entities.ErrorResponder;
import project.persistance.entities.Membership;
import project.persistance.entities.ChatStampReceiver;
import project.persistance.entities.User;
import project.persistance.entities.Chatroom;
import project.persistance.entities.UserResponder;
import project.persistance.entities.ChatroomResponder;
import project.persistance.entities.MembershipResponder;
import project.persistance.entities.ResponderLibrary;
import project.services.ChatroomService;
import project.services.UserService;


/**
 * 
 * @author Vilhelml
 *
 */
@RestController
@RequestMapping("/auth/chatroom")
public class ChatroomController {

	protected final ChatroomService chatroomService;
	protected final UserService userService;
	
	public ChatroomController(ChatroomService chatroomService, UserService userService) {
		this.chatroomService = chatroomService;
		this.userService = userService;
	}
	

	/**
	 * 
	 * @param timestampResponder
	 * @param chatroomName
	 * @return
	 * @deprecated temporary method for testing purposes
	 */
	@RequestMapping(path = "/{chatroomName}/updatechatroomlastmessage", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> updateChatroomLastMessage(@PathVariable String chatroomName) {
		try {
			// fetch the chatroom to be updated
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// set the latest message received as now
			chatroom.setLastMessageReceived((new Date()).getTime());
			// save the changes
			chatroomService.saveChatroom(chatroom);
			// prepare the payload
			ChatroomResponder body = new ChatroomResponder(chatroom);
			// return the payload with a status of 200
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
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
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	/**
	 * @param username: chatroomName of the chatroom to be returned
	 * @return: if chatroom not found: return 404 not found
	 * 			if user is not the owner: return 401 unauthorized
	 * 			if successful: return 204 no content
	 */
	@RequestMapping(path = "/{chatroomName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<Object> deleteChatroom(@PathVariable String chatroomName/*, UsernamePasswordAuthenticationToken token*/){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(/*token.getName()*/"ror9");
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			if(chatroomService.isOwner(user, chatroom)) {
				ErrorResponder body = new ErrorResponder();
				body.setError("User is not the chatroom's owner");
				return new ResponseEntity<>(body.getWrappedError(), HttpStatus.UNAUTHORIZED);
			}
			// wrap the data to send in json format
			
			// DELETE ALL CHAT MESSAGES BEFORE DELETING THE CHATROOM!!!!
			
			chatroomService.deleteChatroom(chatroom);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	/**
	 * 
	 * @param newChatroom, a wrapper for the chatroom data
	 * @return the chatroom that was created, or an error message
	 */
	@RequestMapping(path = "/", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> createChatroom(@RequestBody ChatroomResponder newChatroom, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
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
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	/**
	 * Send an invite from chatroom to user
	 * @param chatroomName: name of the chatroom to send the request
	 * @return: if found, return the chatroom with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/{chatroomName}/invite/{username}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> sendMemberInvitation(@PathVariable String chatroomName, @PathVariable String username, UsernamePasswordAuthenticationToken token){
		try {
			// the user sending the invite (the invitation will be sent by the chatroom, though)
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// the chatroom that the invite is for
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// the user receiving the invite
			User invitee = userService.findByUsername(username);
			// if the invitor doesn't have invite privilages
			if(!chatroomService.hasMemberInvitePrivilages(user, chatroom)) {
				ErrorResponder body = new ErrorResponder();
				body.setError("You do not have permission to invite users to this chatroom.");
				return new ResponseEntity<>(body.getWrappedError(), HttpStatus.UNAUTHORIZED);
			}
			// send the invite
			chatroomService.sendMemberInvite(invitee, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	/**
	 * Join an open chatrom or accept n invite
	 * @param chatroomName: name of the chatroom to be joined
	 * @return: if successful return a status code of 204, else error message with status code of 404 for not found, or 401 for unauthorized
	 */
	@RequestMapping(path = "/{chatroomName}/join", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> joinChatroom(@PathVariable String chatroomName, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// the chatroom that the user wants to join
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// join the room
			chatroomService.joinChatroom(user, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}	
	
	/**
	 * Send an iadmin nvite from chatroom to user
	 * @param chatroomName: name of the chatroom to send the request
	 * @return: if found, return the chatroom with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/{chatroomName}/admininvite/{username}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> sendAdminInvitation(@PathVariable String chatroomName, @PathVariable String username, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// the chatroom that the invite is for
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// the user receiving the invite
			User invitee = userService.findByUsername(username);
			// if the invitor doesn't have invite privilages
			if(!chatroomService.hasAdminInvitePrivilages(user, chatroom)) {
				ErrorResponder body = new ErrorResponder();
				body.setError("You do not have permission to invite admins to this chatroom.");
				return new ResponseEntity<>(body.getWrappedError(), HttpStatus.UNAUTHORIZED);
			}
			if(user == invitee) {
				ErrorResponder body = new ErrorResponder();
				body.setError("Cannot send yourself an admin invitation.");
				return new ResponseEntity<>(body.getWrappedError(), HttpStatus.UNAUTHORIZED);
			}
			// send the invite
			chatroomService.sendAdminInvitation(invitee, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	// accept admin invite
	/**
	 * Join an open chatrom or accept n invite
	 * @param chatroomName: name of the chatroom to be joined
	 * @return: if successful return a status code of 204, else error message with status code of 404 for not found, or 401 for unauthorized
	 */
	@RequestMapping(path = "/{chatroomName}/acceptadmininvite", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> acceptAdminInvite(@PathVariable String chatroomName, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// the chatroom that the user wants to join
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// accept the invite
			chatroomService.acceptAdminInvite(user, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * Leave a chatroom, this includes losing membership and adim status
	 * @param chatroomName
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/leave", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> leaveChatroom(@PathVariable String chatroomName, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// the chatroom that the user wants to join
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// leave the chatroom
			chatroomService.leaveChatroom(user, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	/**
	 * Leave a chatroom, this includes losing membership and adim status
	 * @param chatroomName
	 * @return
	 */
	@RequestMapping(path = "/{chatroomName}/quitadmin", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> quitAdmin(@PathVariable String chatroomName, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// the chatroom that the user wants to join
			Chatroom chatroom = chatroomService.findByChatname(chatroomName);
			// leave the chatroom
			chatroomService.quitAdmin(user, chatroom);
			// return successful, no content 
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	@RequestMapping(path = "/listedchatrooms", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getListedChatrooms(){

		List<Chatroom> chatrooms = chatroomService.getAllListedChatrooms();
		
		// create a list of ChatroomResponders for json return
		List<ChatroomResponder> body = chatrooms.stream().map(x -> new ChatroomResponder(x)).collect(Collectors.toList());
		
		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	@RequestMapping(path = "/chatrooms", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getAllChatrooms(){
		List<Chatroom> chatrooms = chatroomService.getAllChatrooms();
		
		// create a list of ChatroomResponders for json return
		List<ChatroomResponder> body = chatrooms.stream().map(x -> new ChatroomResponder(x)).collect(Collectors.toList());
		
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	// mark a chatroom as read; set when the user last read a message
	@RequestMapping(path = "/markread", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> markChatroomRead(@RequestBody ChatStampReceiver chatStampReceiver, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// fetch the user provided new timestamp
			Long timestamp = chatStampReceiver.getTimestamp();
			// fetch the chatroom the user wants to mark as read
			Chatroom chatroom = chatroomService.findByChatname(chatStampReceiver.getChatroomName());
			// get the membership to update
			Membership membership = chatroomService.getUserMembershipOfChatroom(user, chatroom);
			// update the lastRead timestamp
			membership.setLastRead(timestamp);
			// save the changes
			userService.saveUser(user);
			// prepare the payload
			MembershipResponder body = new MembershipResponder(membership);
			// return the payload with a status of 200
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	// to do
	// mark a chatroom as read; set when the user last read a message
	@RequestMapping(path = "/markmultipleread", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> markMultipleChatroomRead(@RequestBody List<ChatStampReceiver> chatstampReceiver, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			
			List<Membership> memberships = new ArrayList<>();
			
			// update all the chatrooms supplied by user
			for(ChatStampReceiver x : chatstampReceiver) {
				// fetch the user provided new timestamp
				Long timestamp = x.getTimestamp();
				// fetch the chatroom the user wants to mark as read
				Chatroom chatroom = chatroomService.findByChatname(x.getChatroomName());
				// get the membership to update
				Membership membership = chatroomService.getUserMembershipOfChatroom(user, chatroom);
				// update the lastRead timestamp
				membership.setLastRead(timestamp);
				// collect the payloads
				memberships.add(membership);
			}
			
			// save the changes
			userService.saveUser(user);
			
			// convert the memberships into responders for return
			List<MembershipResponder> body = ResponderLibrary.toMembershipResponderList(memberships);
			// return the payload with a status of 200
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
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
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
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
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
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
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
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
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
}
