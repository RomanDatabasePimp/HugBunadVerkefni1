package project.services;

import project.persistance.entities.User;
import project.persistance.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	
	/**
	 * Help function to create maps
	 * key i matches value i
	 * @param keys		string key
	 * @param values	object value
	 * @return			mapped key[i] to value[i] for i=0...n
	 */
	public Map<String, Object> map(String[] keys, Object[] values){
		if(keys.length != values.length) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		for(int i = 0; i<keys.length; i++) {
			result.put(keys[i], values[i]);
		}
		return result;
	}

	/**
	 * Helper function that returns an error message denoting that the user was not found
	 * @return map object denoting an error message
	 */
	public Map<String, Object> userNotFound(){
		return map(
			new String[] {"error"}, 
			new String[] {"User not found."}
		);
	}
	
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
	
	public Map<String, Object> createUser(User newUser) {
		User user = userRepository.save(newUser);
		return map(
			new String[] {"userName", "displayName", "email", "created"},
			new Object[] {user.getUserName(), user.getDisplayName(), user.getEmail(), user.getCreated()}
    	);
	}
	
	/**
	 * returns a user if the userName is in use
	 * else, returns and error message
	 * @param userName
	 * @return Map object containing user or error message
	 * @throws exception if userName doesn't belong to any user
	 */
	@Transactional(readOnly = true)
    public Map<String, Object> findByUserName(String userName) throws Exception{
		// throw error if user doesn't exist
		if(!userExists(userName)) {
			throw new Exception("User not found");
		}
		
		User user = this.userRepository.findByUserName(userName);
		
        return map(
			new String[] {"userName", "displayName", "email", "created"},
			new Object[] {user.getUserName(), user.getDisplayName(), user.getEmail(), user.getCreated()}
    	);
    }

	@Transactional(readOnly = true)
    public Map<String, Object> getUserFriends(String userName) throws Exception{
		// throw error if user doesn't exist
		if(!userExists(userName)) {
			throw new Exception("User not found");
		}
		
		User user = this.userRepository.findByUserName(userName);

		List<Map<String, Object>> friends = new ArrayList<>();
		
		for (User f : user.getFriends()) {

			Map<String, Object> friend = map(
				new String[] {"userName", "displayName", "created"}, 
				new Object[] {f.getUserName(), f.getDisplayName(), f.getCreated()}
			);
			
			int source = friends.indexOf(friend);
			if (source == -1) {
				friends.add(friend);
			}
		}
		
		return map(
			new String[] {"friends"},
			new Object[] {friends}
		);

    }
	
	
}
