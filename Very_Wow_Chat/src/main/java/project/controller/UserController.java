package project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Map;

import project.services.UserService;
import project.persistance.entities.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/{userName}/friends")
    public Map<String, Object> getFriends(@PathVariable String userName) {
		return userService.getUserFriends(userName);
		//Collection<User> friends = this.userService.getUserFriends(userName);
		
	}
}
