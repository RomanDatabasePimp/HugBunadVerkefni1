package project.persistance.entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@NodeEntity
public class User {

	@Id @GeneratedValue private Long id;

	private String userName;
	private String password;
	private String displayName;
	private String email;
	private Long created;
	
	// The User's friends
	@Relationship(type="FRIENDS", direction=Relationship.UNDIRECTED)
	private List<User> friends;
	
	// Users who have received a friend request from the User
	@Relationship(type="FRIEND_REQUEST", direction=Relationship.OUTGOING)
	private List<User> friendRequestees;
	
	// Users who have sent a friend request to the User
	@Relationship(type="FRIEND_REQUEST", direction=Relationship.INCOMING)
	private List<User> friendRequestors;
	
	// Empty constructor required as of Neo4j API 2.0.5
	private User() {};

	/**
	 * Create a new user
	 * @param userName		a unique user name used to authenticate user
	 * @param password		user's password, used to authenticate user
	 * @param displayName	user's display name, seen by other users
	 */
	public User(String userName, String password, String displayName, String email) {
		this.userName = userName;
		this.password = password;
		this.displayName = displayName;
		this.email = email;
		this.created = (new Date()).getTime(); // current time
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getEmail() {
		return email;
	}

	public Long getCreated() {
		return created;
	}

	public List<User> getFriends() {
		return friends;
	}

	public List<User> getFriendRequestees() {
		return friendRequestees;
	}

	public List<User> getFriendRequestors() {
		return friendRequestors;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	
	
//	/**
//	 * Send a friend request to another user
//	 * @param requestee		User to receive friend request
//	 */
//	public void sendFriendRequest(User requestee) throws Exception{
//		if(this.friendRequestees == null) {
//			this.friendRequestees = new HashSet<User>();
//		}
//		// check if requestee is already a friend
//		if(this.friends.contains(requestee)) {
//			throw new Exception("Requestee is already a friend.");
//		}
//		// check if requestee has already received a friend request
//		if(this.friendRequestees.contains(requestee)) {
//			throw new Exception("Requestee already has a pending friend request.");
//		}
//		// create friend relation
//		this.friendRequestees.add(requestee);
//		requestee.friendRequestors.add(this);
//	}
//	
//	/**
//	 * Accept a friend request from another user
//	 * @param requestee 	User whose friend request is to be accepted
//	 * @throws Exception	if no friend request exists from requestee
//	 */
//	public void acceptFriendRequest(User requestor) throws Exception{
//		if(this.friendRequestors == null) {
//			this.friendRequestors = new HashSet<User>();
//		}
//		// check if requestee has sent a friend request to accept
//		if(!this.friendRequestors.contains(requestor)) {
//			throw new Exception("Requestor has not sent a friend request.");
//		}
//		// delete FRIEND_REQUEST relation
//		this.friendRequestors.remove(requestor);
//		requestor.friendRequestees.remove(this);
//		// add FRIENDS relation
//		this.addFriend(requestor);
//	}
//	
//	/**
//	 * 
//	 * @param friend		User to be befriended
//	 * @throws Exception	if friend is already a friend
//	 */
//	private void addFriend(User friend) throws Exception{
//		if(this.friends == null) {
//			this.friends = new HashSet<User>();
//		}
//		if(this.friends.contains(friend)) {
//			throw new Exception("Already friends.");
//		}
//		this.friends.add(friend);
//		friend.friends.add(this);
//	}
//
//	/**
//	 * Decline a friend request from another user
//	 * @param requestor		User whose friend request is to be declined
//	 */
//	public void declineFriendRequest(User requestor) throws Exception{
//		if(this.friendRequestors == null) {
//			this.friendRequestors = new HashSet<User>();
//		}
//		// check if requestor has sent a friend request
//		if(!this.friendRequestors.contains(requestor)) {
//			throw new Exception("Requestor has not sent a friend request!");
//		}
//		// delete FRIEND_REQUEST relation
//		this.friendRequestors.remove(requestor);
//		requestor.friendRequestees.remove(this);
//	}
//	
//	/**
//	 * Remove a friend from friends list
//	 * @param friend	friend to me removed from friends list
//	 */
//	public void removeFriend(User friend) throws Exception{
//		if(this.friends == null || !this.friends.contains(friend)) {
//			throw new Exception("Removee is not a friend!");
//		}
//		this.friends.remove(friend);
//		friend.friends.remove(this);
//	}
}
