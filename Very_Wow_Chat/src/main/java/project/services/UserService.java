package project.services;

import project.persistance.entities.User;
import project.persistance.entities.Friendship;
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
	
	private Map<String, Object> toD3Format(Collection<User> users) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<User> result = users.iterator();
		while (result.hasNext()) {
			User user = result.next();
			nodes.add(map(
				new String[] {"label", "userName", "displayName", "created"}, 
				new Object[] {"User", user.getUserName(), user.getDisplayName(), user.getCreated()}
			));
			
			int target = i;
			i++;
			for (Friendship friendship : user.getFriendships()) {
				User f = friendship.getOtherUser(user);

				Map<String, Object> friend = map(
					new String[] {"label", "userName", "displayName", "created", "friendsSince"}, 
					new Object[] {"Friend", f.getUserName(), f.getDisplayName(), f.getCreated(), friendship.getDate()}
				);
				
				int source = nodes.indexOf(friend);
				if (source == -1) {
					nodes.add(friend);
					source = i++;
				}
				Map<String, Object> rel = new HashMap<String, Object>();
				
				rels.add(map(
						new String[] {"source", "target"},
						new Object[] {source, target}
				));
			}
		}
		return map(
			new String[] {"nodes", "links"},
			new Object[] {nodes, rels}
		);
	}
	
	/**
	 * Help function to create maps
	 * key i matches value i
	 * @param keys		string key
	 * @param values	object value
	 * @return			mapped key[i] to value[i] for i=0...n
	 */
	private Map<String, Object> map(String[] keys, Object[] values){
		if(keys.length != values.length) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		for(int i = 0; i<keys.length; i++) {
			result.put(keys[i], values[i]);
		}
		return result;
	}
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Transactional(readOnly = true)
    public Map<String, Object> findByUserName(String userName) {
		User user = this.userRepository.findByUserName(userName);
		
        return map(
			new String[] {"userName", "displayName", "created"},
			new Object[] {user.getUserName(), user.getDisplayName(), user.getCreated()}
    	);
    }

	@Transactional(readOnly = true)
    public Map<String, Object> getUserFriends(String userName) {
        Collection<User> friends = this.userRepository.getUserFriends(userName);
        return toD3Format(friends);
    }
	
	
}
