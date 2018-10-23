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
	private Long created;
	
	// The User's friends
	@Relationship(type="FRIENDS", direction=Relationship.UNDIRECTED)
	private Set<Friendship> friendships;
	
	// Users who have received a friend request from the User
	@Relationship(type="FRIEND_REQUEST", direction=Relationship.OUTGOING)
	private Set<User> friendRequestees;
	
	// Users who have sent a friend request to the User
	@Relationship(type="FRIEND_REQUEST", direction=Relationship.INCOMING)
	private Set<User> friendRequestors;
	
	// Empty constructor required as of Neo4j API 2.0.5
	private User() {};

	/**
	 * Create a new user
	 * @param userName		a unique user name used to authenticate user
	 * @param password		user's password, used to authenticate user
	 * @param displayName	user's display name, seen by other users
	 */
	public User(String userName, String password, String displayName) {
		this.userName = userName;
		this.password = password;
		this.displayName = displayName;
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

	public Long getCreated() {
		return created;
	}

	public Set<Friendship> getFriendships() {
		return friendships;
	}

	public Set<User> getFriendRequestees() {
		return friendRequestees;
	}

	public Set<User> getFriendRequestors() {
		return friendRequestors;
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
