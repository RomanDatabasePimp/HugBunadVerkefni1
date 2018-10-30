package project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import project.services.UserService;
import project.persistance.entities.User;
import project.persistance.entities.UserResponder;
import project.persistance.entities.ErrorResponder;

/**
 * 
 * @author Vilhelml
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	/* 
	 * TEMPORARY controller for debugging purposes
	 * creates a user
	*/
	@RequestMapping(path = "/", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> createUser(@RequestBody UserResponder newUser){
		try {
			User user = new User(
				newUser.getUsername(),
				newUser.getPassword(),
				newUser.getDisplayName(),
				newUser.getEmail()
			);
			System.out.println(user.getDisplayName() + "pre save");
			userService.createUser(user);
			System.out.println(user.getDisplayName() + "post save");
			UserResponder body = new UserResponder(user);
			return new ResponseEntity<>(body, HttpStatus.CREATED);
		}catch(IllegalArgumentException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update a user's displayName, password, and email
	 * @param newUser: container for the properties to update
	 * @return
	 */
	@RequestMapping(path = "/", method = RequestMethod.PATCH, headers = "Accept=application/json")
    public ResponseEntity<Object> updateUser(@RequestBody UserResponder newUser){
		try {
			// fetch the user
			User user = userService.findByUsername("username1"); // get from token
			// if an attribute is not given, the old one is used
			String newDisplayName = newUser.getDisplayName() != null ? newUser.getDisplayName() : user.getDisplayName();
			String newEmail = newUser.getEmail() != null ? newUser.getEmail() : user.getEmail();
			String newPassword = newUser.getPassword() != null ? newUser.getPassword() : user.getPassword();
			// apply the new attributes
			user.setDisplayName(newDisplayName);
			user.setPassword(newPassword);
			user.setEmail(newEmail);
			// save the changes
			userService.saveUser(user);
			// wrap the user and return it
			UserResponder body = new UserResponder(user);
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(path = "/", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> deleteUser(){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		User user = userService.findByUsername("username1"); // get from token
		userService.deleteUser(user);
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	/**
	 * Delete a friend request from authorized user to the given requestee
	 * @param friendName
	 * @return no content or error
	 * 
	 * Pæling: hafa þessa virkni í delete friend? þannig að delete friend eyði vini eða request eftir aðstæðum
	 */
	@RequestMapping(path = "/friendRequest/{requesteeName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> deleteFriendRequest( @PathVariable String requesteeName){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		try {
			User user = userService.findByUsername("username1"); // get from token
			User requestee = userService.findByUsername(requesteeName);

			userService.deleteFriendRequest(user, requestee);
			
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(IllegalArgumentException e){
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}catch(NoSuchElementException e){
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Delete a friend relation between authorized user and the given friend
	 * @param friendName
	 * @return no content or error
	 */
	@RequestMapping(path = "/friends/{friendName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<Object> deleteFriend( @PathVariable String friendName){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		try {
			User user = userService.findByUsername("username1"); // get from token
			User friend = userService.findByUsername(friendName);

			userService.deleteFriendship(user, friend);
			
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch(IllegalArgumentException e){
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}catch(NoSuchElementException e){
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}

	// add friend: send friend request / accept friend request
	@RequestMapping(path = "/friends/{friendName}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> addFriend(@PathVariable String friendName){
		Boolean invalidToken = false;
		if(invalidToken/*invalid token*/) {
			ErrorResponder body = new ErrorResponder();
			body.setError("Invalid token.");
			return new ResponseEntity<>(body.getError(), HttpStatus.UNAUTHORIZED);
		}
		try {
			User user = userService.findByUsername("username1"); // get from token !!!
			User friend = userService.findByUsername(friendName);
			
			userService.addFriend(user, friend);
			
			// whether friend request was sent or a friendship was created, no content is returned
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
		}catch(IllegalArgumentException e){
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}catch(NoSuchElementException e){
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * getUser: 
	 * @param username: username of the user to be returned
	 * @return: if found, return the user with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/{username}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getUser(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			// wrap the data to send in jsopn format
			UserResponder body = new UserResponder(user);
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) { // user was not found
			// use wrapper to return error
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	
	// GET request to this url will return a list of all the user's friends
	@RequestMapping(path = "/{username}/friends")
    public ResponseEntity<Object> getFriends(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<User> friends = user.getFriends();
			
			// create a list of UserResponders for json return
			List<UserResponder> body = friends.stream().map(x -> new UserResponder(x)).collect(Collectors.toList());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	// GET request to this url will return a list of all the user's friend requestees
	@RequestMapping(path = "/{username}/friendRequestees")
    public ResponseEntity<Object> getFriendRequestees(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<User> requestees = user.getFriendRequestees();
			
			// create a list of UserResponders for json return
			List<UserResponder> body = requestees.stream().map(x -> new UserResponder(x)).collect(Collectors.toList());
			
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	
	// GET request to this url will return a list of all the user's friend requestees
	@RequestMapping(path = "/{username}/friendRequestors")
    public ResponseEntity<Object> getFriendRequestors(@PathVariable String username){
		try {
			User user = userService.findByUsername(username);
			List<User> requestors = user.getFriendRequestors();
			
			// create a list of UserResponders for json return
			List<UserResponder> body = requestors .stream().map(x -> new UserResponder(x)).collect(Collectors.toList());
			
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
}
