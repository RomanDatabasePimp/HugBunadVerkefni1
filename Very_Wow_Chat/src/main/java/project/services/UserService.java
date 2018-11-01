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

	// logs all neo4j calls
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * Check if a user exists with a given username
	 * @param username	a user's userName
	 * @return true if userName is in use, else false
	 */
	public boolean userExists(String username) {
		User user = this.userRepository.findByUsername(username);
		return user != null;
	}


	/**
	 * save a user, used to apply updates
	 * @param user the user to be updated
	 * @return
	 */
	public User saveUser(User user){
		// save the user in database
		return userRepository.save(user);
	}
	/**
	 * create a a user
	 * @param newUser
	 * @return the new user
	 * @throws IllegalArgumentException if username is taken
	 */
	public User createUser(User newUser) throws IllegalArgumentException{
		// throw error if username is taken
		if(userExists(newUser.getUsername())) {
			throw new IllegalArgumentException("Username is already in use.");
		}
		User user = userRepository.save(newUser);
		return user;
	}
	
	/**
	 * returns a user if the username is in use
	 * else, returns and error message
	 * @param username
	 * @return the user
	 * @throws exception if userName doesn't belong to any user
	 */
	@Transactional(readOnly = true)
    public User findByUsername(String username) throws NoSuchElementException{
		// throw error if user doesn't exist
		if(!userExists(username)) {
			throw new NoSuchElementException("User not found");
		}
		
		User user = this.userRepository.findByUsername(username);
		
		return user;
    }
	
	/**
	 * delete a user and all its relations
	 * @param user: user to be deleted
	 */
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	
	/**
	 * Add a friend: sends a friend request, or creates a friend relation if requestee has already sent a friend request
	 * @param requestor: user sending the request
	 * @param requestee user receiving the request
	 * @throws IllegalArgumentException
	 */
	@Transactional(readOnly = false)
	public void addFriend(User requestor, User requestee) throws IllegalArgumentException{
		System.out.println("send request");
		System.out.println(requestor.getUsername() + " -[request]-> " + requestee.getUsername());
		// check if user is sending himself a friend request
		if(requestor == requestee) {
			throw new IllegalArgumentException("Cannot add self as friend.");
		}
		// check if a friend request has already been sent
		if(friendRequestSent(requestor, requestee)) {
			throw new IllegalArgumentException("A friend request is already pending.");
		}
		// check if they are already friends
		if(areFriends(requestor, requestee)) {
			throw new IllegalArgumentException("おまえ は もう 友達。(You are already friends)");
		}
		// check if a friend requet has been sent in the other direction already
		if(friendRequestSent(requestee, requestor)) {
			System.out.println("friend request from other direction has been sent");
			// both users have sent each other a friend request, they are now friends
			// delete the old friend request
			deleteFriendRequest(requestee, requestor);
			// create friend relation
			createFriendRelation(requestee, requestor);
			return;
		}
		// send a friend request
		sendFriendRequest(requestor, requestee);
	}
	
	/**
	 * Delete a friend request from requestor to requestee
	 * @param requestor: the original requestor of the request
	 * @param requestee: the original requestee of the request
	 */
	@Transactional(readOnly = false)
	public void deleteFriendRequest(User requestor, User requestee) throws NoSuchElementException {
		if(!friendRequestSent(requestor, requestee)) {
			throw new IllegalArgumentException("There is no friend request to delete");
		}
		List<User> requestorRequestees = requestor.getFriendRequestees();
		List<User> requesteeRequestors = requestee.getFriendRequestors();

		// delete the request
		requestorRequestees.remove(requestee);
		requesteeRequestors.remove(requestor);
		// save both users so the database will be updated and the request deleted
		userRepository.save(requestee);
		userRepository.save(requestor);
	}

	/**
	 * Delete a friend relation between 2 users
	 * @param use1
	 * @param user2
	 */
	@Transactional(readOnly = false)
	public void deleteFriendship(User user1, User user2) throws NoSuchElementException {
		if(!areFriends(user1, user2)) {
			throw new IllegalArgumentException("There is no friend relation to delete");
		}
		List<User> user1Friends = user1.getFriends();
		List<User> user2Friends = user2.getFriends();

		// remove the users from each other's friendlist
		user1Friends.remove(user2);
		user2Friends.remove(user1);
		// save both users so the database will be updated and the friendship was deleted
		userRepository.save(user1);
		userRepository.save(user2);
	}

	/**
	 * Checks if user1 and user2 are friends
	 * @param user1
	 * @param user2
	 * @return: true if they are friends, else returns false
	 */
	@Transactional(readOnly = false)
	public Boolean areFriends(User user1, User user2) {
		List<User> user1Friends = user1.getFriends();
		List<User> user2Friends = user2.getFriends();

		// data invariability: if the former condition is true, then the latter should also be, 
		// if the former is false, then the latter should also be
		return user1Friends.contains(user2) && user2Friends.contains(user1);
	}

	/**
	 * Send a friend request
	 * @param requestor: user sending the request
	 * @param requestee user receiving the request
	 * @throws IllegalArgumentException if a request is already pending
	 */
	private void sendFriendRequest(User requestor, User requestee) throws IllegalArgumentException{
		// get the requestors and requestees
		List<User> requestorRequestees = requestor.getFriendRequestees();
		List<User> requesteeRequestors = requestee.getFriendRequestors();
		
		// check if a friend request has already been sent
		if(friendRequestSent(requestor, requestee)) {
			throw new IllegalArgumentException("A friend request is already pending.");
		}
		
		// create the friend request
		requestorRequestees.add(requestee);
		requesteeRequestors.add(requestor);
		
		// save the relation in database
		userRepository.save(requestee);
		userRepository.save(requestor);
	}
	
	/**
	 * Checks requestor has sent requestee a friend request
	 * @param requestor: user sending the request
	 * @param requestee user receiving the request
	 * @return true if a friend request is pending, else returns false
	 */
	@Transactional(readOnly = false)
	public Boolean friendRequestSent(User requestor, User requestee) {
		List<User> requestorRequestees = requestor.getFriendRequestees();
		List<User> requesteeRequestors = requestee.getFriendRequestors();
		
		// data invariability: if the former condition is true, then the latter should also be, 
		// if the former is false, then the latter should also be
		return requestorRequestees.contains(requestee) && requesteeRequestors.contains(requestor);
	}
	
	/**
	 * Create a friend relation between 2 users
	 * @param user1
	 * @param user2
	 * @throws IllegalArgumentException: if users are already friends or if it's the same user
	 */
	private void createFriendRelation(User user1, User user2) throws IllegalArgumentException{
		if(user1 == user2) {
			throw new IllegalArgumentException("Cannot add self as friend.");
		}
		if(areFriends(user1, user2)) {
			throw new IllegalArgumentException("Users are already friends.");
		}
		List<User> user1Friends = user1.getFriends();
		List<User> user2Friends = user2.getFriends();
		
		user1Friends.add(user2);
		user2Friends.add(user1);

		// save the relation in database
		userRepository.save(user1);
		userRepository.save(user2);
	}
}
