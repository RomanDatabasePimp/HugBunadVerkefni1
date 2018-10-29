package project.services;

import project.persistance.entities.User;
import project.persistance.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class UserService {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * Check if a user exists with a given username
	 * @param userName	a user's userName
	 * @return true if userName is in use, else false
	 */
	public boolean userExists(String userName) {
		User user = this.userRepository.findByUserName(userName);
		return user != null;
	}
	
	public User createUser(User newUser) throws IllegalArgumentException{
		// throw error if username is taken
		if(!userExists(newUser.getUserName())) {
			throw new IllegalArgumentException("Username is already in use.");
		}
		User user = userRepository.save(newUser);
		return user;
	}
	
	/**
	 * returns a user if the userName is in use
	 * else, returns and error message
	 * @param userName
	 * @return Map object containing user or error message
	 * @throws exception if userName doesn't belong to any user
	 */
	@Transactional(readOnly = true)
    public User findByUserName(String userName) throws NoSuchElementException{
		// throw error if user doesn't exist
		if(!userExists(userName)) {
			throw new NoSuchElementException("User not found");
		}
		
		User user = this.userRepository.findByUserName(userName);
		
		return user;
    }
}
