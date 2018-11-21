package project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import project.services.ChatroomService;
import project.services.CryptographyService;
import project.services.UserService;
import project.persistance.entities.User;
import project.errors.HttpException;
import project.payloads.ChatroomResponder;
import project.payloads.MembershipResponder;
import project.payloads.RelationsResponder;
import project.payloads.ResponderLibrary;
import project.payloads.ResponseWrapper;
import project.payloads.UserFullResponder;
import project.payloads.UserResponder;
import project.payloads.UserUpdateReceiver;
import project.persistance.entities.Chatroom;
import project.persistance.entities.Membership;


/**
 * 
 * @author Vilhelml
 */
@RestController
@RequestMapping("/auth/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatroomService chatroomService;

	/**
	 * Update a user's displayName, password, and email
	 * @param newUser: container for the properties to update
	 * @return
	 */
	@RequestMapping(path = "/", method = RequestMethod.PATCH, headers = "Accept=application/json")
    public ResponseEntity<Object> updateUser(@RequestBody UserUpdateReceiver newUser, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// if an attribute is not given, the old one is used
			String newDisplayName = newUser.getDisplayName() != null ? newUser.getDisplayName() : user.getDisplayName();
			// TODO: encrypt email
			String newEmail = newUser.getEmail() != null ? CryptographyService.getCiphertext(newUser.getEmail()) : user.getEmail();
			String newPassword = newUser.getPassword() != null ? newUser.getPassword() : user.getPassword();
			// apply the new attributes
			user.setDisplayName(newDisplayName);
			user.setPassword(newPassword);
			user.setEmail(newEmail);
			// save the changes
			userService.saveUser(user);
			// wrap the user and return it
			UserResponder body = new UserResponder(user);
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * delete the user
	 * @return nodata, 204 status
	 */
	@RequestMapping(path = "/", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> deleteUser(UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			
			// delete all the user's chatrooms
			List<Chatroom> chatrooms = user.getOwnedChatrooms();
			for(Chatroom chatroom : chatrooms) {
				this.chatroomService.deleteChatroom(chatroom);
			}
			// delete the user
			userService.deleteUser(user);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);			
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * Delete a friend request from authorized user to the given requestee
	 * @param friendName
	 * @return no content or error
	 * 
	 */
	@RequestMapping(path = "/friendRequest/{requestorName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> deleteFriendRequest( @PathVariable String requestorName, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			User requestor = userService.findByUsername(requestorName);

			userService.deleteFriendRequest(requestor, user);
			
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * Delete a friend relation between authorized user and the given friend
	 * @param friendName
	 * @return no content or error
	 */
	@RequestMapping(path = "/friends/{friendName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> deleteFriend( @PathVariable String friendName, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			User friend = userService.findByUsername(friendName);

			userService.deleteFriendship(user, friend);
			
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	// add friend: send friend request / accept friend request
	@RequestMapping(path = "/friends/{friendName}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> addFriend(@PathVariable String friendName, UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			User friend = userService.findByUsername(friendName);
			
			userService.addFriend(user, friend);
			
			// whether friend request was sent or a friendship was created, no content is returned
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	/**
	 * @param username: username of the user to be returned
	 * @return: if found, return the user with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/{username}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getUser(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			// wrap the data to send in json format
			UserResponder body = new UserResponder(user);
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	/**
	 * @param username: username of the user to be returned
	 * @return: if found, return the user with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getSelfInfo(UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			// wrap the data to send in json format
			UserFullResponder body = new UserFullResponder(user);
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	/**
	 * @param username: username of the user to be returned
	 * @return: if found, return the user with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/getallrelations",method = RequestMethod.GET,headers = "Accept=application/json")
    public ResponseEntity<Object> getAllRelations(UsernamePasswordAuthenticationToken token){
		try {
			// fetch user from authentication token
			User user = userService.findByUsername(token.getName());
			
			// get the user's relations
			List<User> friends = user.getFriends();
			List<User> friendRequestors = user.getFriendRequestors();
			List<User> friendRequestees = user.getFriendRequestees();
			List<Chatroom> chatroomAdminInvites = user.getChatroomAdminInvites();
			List<Chatroom> chatroomInvites = user.getChatroomInvites();
			List<Chatroom> chatroomRequests = user.getChatroomRequests();
			List<Membership> memberships = user.getMemberships(); // memberOfChatrooms, adminOfChatrooms, and ownedChatrooms are combined into this list
			
			// convert the users and chatrooms lists to responder lists
			List<UserResponder> friendsResponderList = ResponderLibrary.toUserResponderList(friends);
			List<UserResponder> friendRequestorsResponderList = ResponderLibrary.toUserResponderList(friendRequestors);
			List<UserResponder> friendRequesteesResponderList = ResponderLibrary.toUserResponderList(friendRequestees);
			List<ChatroomResponder> chatroomAdminInvitesResponderList = ResponderLibrary.toChatroomResponderList(chatroomAdminInvites);
			List<ChatroomResponder> chatroomInvitesResponderList = ResponderLibrary.toChatroomResponderList(chatroomInvites);
			List<ChatroomResponder> chatroomRequestsResponderList = ResponderLibrary.toChatroomResponderList(chatroomRequests);
			List<MembershipResponder> membershipsResponderList = ResponderLibrary.toMembershipResponderList(memberships);

			// wrap the responders in a container responder
			RelationsResponder body = new RelationsResponder();
			body.add("friends", friendsResponderList);
			body.add("friendRequestors", friendRequestorsResponderList);
			body.add("friendRequestees", friendRequesteesResponderList);
			body.add("chatroomAdminInvites", chatroomAdminInvitesResponderList);
			body.add("chatroomInvites", chatroomInvitesResponderList);
			body.add("chatroomRequests", chatroomRequestsResponderList);
			body.add("chatrooms", membershipsResponderList);

			// return the responder with a 201 status
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	// --------------- get requests for the user's various lists ----------------------------------
	
	// GET request to this url will return a list of all the user's friends
	@RequestMapping(path = "/{username}/friends", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getFriends(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<User> friends = user.getFriends();
			
			// create a list of UserResponders for json return
			List<UserResponder> body = ResponderLibrary.toUserResponderList(friends);

			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	// GET request to this url will return a list of all the user's friend requestees
	@RequestMapping(path = "/{username}/friendrequestees", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getFriendRequestees(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<User> requestees = user.getFriendRequestees();
			
			// create a list of UserResponders for json return
			List<UserResponder> body = ResponderLibrary.toUserResponderList(requestees);
			
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	// GET request to this url will return a list of all the user's friend requestees
	@RequestMapping(path = "/{username}/friendrequestors")
    public ResponseEntity<Object> getFriendRequestors(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<User> requestors = user.getFriendRequestors();
			
			// create a list of UserResponders for json return
			List<UserResponder> body = ResponderLibrary.toUserResponderList(requestors);
			
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	// GET request to this url will return a list of all the user's friend requestees
	@RequestMapping(path = "/{username}/memberofchatrooms", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getMemberOfChatrooms(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<Chatroom> chatrooms = user.getMemberOfChatrooms();
			
			// create a list of UserResponders for json return
			List<ChatroomResponder> body = ResponderLibrary.toChatroomResponderList(chatrooms);
			
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	@RequestMapping(path = "/{username}/adminofchatrooms", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getAdminOfChatrooms(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<Chatroom> chatrooms = user.getAdminOfChatrooms();
			
			// create a list of UserResponders for json return
			List<ChatroomResponder> body = ResponderLibrary.toChatroomResponderList(chatrooms);
			
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	@RequestMapping(path = "/{username}/ownerofchatrooms", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getOwnerOfChatrooms(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<Chatroom> chatrooms = user.getOwnedChatrooms();
			
			// create a list of UserResponders for json return
			List<ChatroomResponder> body = ResponderLibrary.toChatroomResponderList(chatrooms);
			
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	@RequestMapping(path = "/{username}/chatroominvites", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getChatroomInvites(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<Chatroom> chatrooms = user.getChatroomInvites();
			
			// create a list of UserResponders for json return
			List<ChatroomResponder> body = ResponderLibrary.toChatroomResponderList(chatrooms);
			
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}

	@RequestMapping(path = "/{username}/chatroomadmininvites", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getChatroomAdminInvites(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<Chatroom> chatrooms = user.getChatroomAdminInvites();
			
			// create a list of UserResponders for json return
			List<ChatroomResponder> body = ResponderLibrary.toChatroomResponderList(chatrooms);
			
			return new ResponseEntity<>(ResponseWrapper.wrap(body), HttpStatus.OK);
		}catch(HttpException e) {
			return e.getErrorResponseEntity();
		}
	}
	
	// add lastRead relation from user to chatroom
	// add last message receiver attribute hjá chatroom
	
	// get all users
	
	/**
	 * /user/{username}/getallrelations
	 * get all user relations
	 * friends: user[] // only displayname, and username, maybe created
	 * friendRequestees: user[]
	 * friendrequestors: user[]
	 * memberOfChatrooms: chatroom[]
	 * adminOfChatrooms: chatroom[]
	 * ownerOfChatrooms: chatroom[]
	 * chatoomMemberInvites: chatroom[]
	 * chatroomAdminInvites: chatroom[]
	 * bannedFromChatrooms (?)
	 */

}
