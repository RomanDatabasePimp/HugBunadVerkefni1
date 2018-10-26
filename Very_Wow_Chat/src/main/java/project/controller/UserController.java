package project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import project.services.UserService;
import project.persistance.entities.User;
import project.persistance.entities.ErrorResponder;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * getUser: 
	 * @param username: username of the user to be returned
	 * @return: if found, return the user with a status code of 200, else error message with status code of 404
	 */
	@RequestMapping(path = "/{username}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Object> getUser(@PathVariable String username){
		try {
			User user = userService.findByUserName(username);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}catch(NoSuchElementException e) { // user was not found
			// user wrapper to return error
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(path = "/{userName}/friends")
    public ResponseEntity<Object> getFriends(@PathVariable String username) throws Exception{
		try {
			User user = userService.findByUserName(username);
			List<User> friends = user.getFriendRequestees();
			return new ResponseEntity<>(friends, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(path = "/", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Object> registerUser(@RequestBody User newUser) throws Exception{
		try {
			User user = userService.createUser(newUser);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		}catch(IllegalArgumentException e) {
			ErrorResponder body = new ErrorResponder();
			body.setError(e.getMessage());
			return new ResponseEntity<>(body.getError(), HttpStatus.BAD_REQUEST);
		}
	}
}
