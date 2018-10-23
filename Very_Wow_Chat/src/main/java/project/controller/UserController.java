package project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Collection;
import java.util.Map;

import project.services.UserService;
import project.persistance.entities.User;
import project.persistance.entities.UserRegistrationFormReceiver;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/{userName}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String userName) throws Exception{
		if(!userService.userExists(userName)) {
			Map<String, Object> body = userService.userNotFound();
			return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
		}
		Map<String, Object> body = userService.findByUserName(userName);
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	
	@RequestMapping(path = "/{userName}/friends")
    public ResponseEntity<Map<String, Object>> getFriends(@PathVariable String userName) throws Exception{
		if(!userService.userExists(userName)) {
			Map<String, Object> body = userService.userNotFound();
			return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
		}
		Map<String, Object> body = userService.getUserFriends(userName);
		return new ResponseEntity<>(body, HttpStatus.OK);
		
	}
	
	@RequestMapping(path = "/", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationFormReceiver regitration) throws Exception{
		if(userService.userExists(regitration.getUserName())) {
			Map<String, Object> body = userService.map(
				new String[] {"error"},
				new Object[] {"Username already in use."}
	    	);
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}
		
		User user = new User(regitration.getUserName(), regitration.getPassword(), regitration.getDisplayName(), regitration.getEmail());
		
		Map<String, Object> body = userService.createUser(user);
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}
}
