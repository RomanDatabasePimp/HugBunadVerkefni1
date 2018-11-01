package project.services;

import project.persistance.entities.Chatroom;
import project.persistance.entities.User;
import project.persistance.repositories.ChatroomRepository;
import project.persistance.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import project.Errors.UnauthorizedException;

@Service
public class ChatroomService {
	// logs all neo4j calls
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);

	private final ChatroomRepository chatroomRepository ;
	//private final UserRepository userRepository ;
	
	public ChatroomService(ChatroomRepository chatroomRepository/*, UserRepository userRepository*/) {
		this.chatroomRepository = chatroomRepository;
		//this.userRepository = userRepository;
	}

	/**
	 * Check if a chatroom exists with a given chatroomName
	 * @param chatroomName	a user's userName
	 * @return true if chatroomName is in use, else false
	 */
	public Boolean chatroomExists(String chatroomName) {
		Chatroom chatroom = this.chatroomRepository.findByChatroomName(chatroomName);
		return chatroom != null;
	}

	/**
	 * returns a chatroom if the chatroomName is in use
	 * else, returns and error message
	 * @param chatroomName
	 * @return the chatroom
	 * @throws exception if chatroomName doesn't belong to any chatroom
	 */
	public Chatroom findByChatname(String chatroomName)  throws NoSuchElementException{
		// throw error if user doesn't exist
		if(!chatroomExists(chatroomName)) {
			throw new NoSuchElementException("Chatroom not found");
		}
		
		Chatroom chatroom = this.chatroomRepository.findByChatroomName(chatroomName);
		
		return chatroom;
    }
	
	/**
	 * create a chatroom
	 * @param newChatroom
	 * @return the newly created chatroom
	 */
	@Transactional(readOnly = false)
	public Chatroom createChatroom(User user, Chatroom newChatroom) {
		// throw error if username is taken
		if(chatroomExists(newChatroom.getChatroomName())) {
			throw new IllegalArgumentException("Chatoom name is already in use.");
		}
		// create a owner relation
		newChatroom.setOwner(user);
		// add chatroom to user's list of owned chatrooms
		List<Chatroom> ownedRooms = user.getOwnedChatrooms();
		ownedRooms.add(newChatroom);
		
		// create admin relation
		createAdminRelation(user, newChatroom);
		
		//create member relation
		createMemberRelation(user, newChatroom);
		
		// save the chatroom, this will also save the user's new relations
		Chatroom chatroom = chatroomRepository.save(newChatroom);
		return chatroom;
	}
	
	/**
	 * save a chatroom, used to apply updates
	 * @param chatroom
	 * @return the newly updates
	 */
	@Transactional(readOnly = false)
	public Chatroom saveChatroom(Chatroom chatroom) {
		return chatroomRepository.save(chatroom);
	}
	
	/**
	 * if 
	 * @param user
	 * @param chatroom
	 * @return
	 */
	public boolean hasMemberInvitePrivilages(User user, Chatroom chatroom) {
		return isOwner(user, chatroom) || isAdmin(user, chatroom);
	}
	/**
	 * join a chatroom
	 * @param user
	 * @param chatroom
	 * @throws IllegalArgumentException if you are already a member of the chatroom
	 * @throws UnauthorizedException if you don't have permission to join
	 */
	@Transactional(readOnly = false)
	public void joinChatroom(User user, Chatroom chatroom) throws IllegalArgumentException, UnauthorizedException {
		// already member
		if(isMember(user, chatroom)) {
			throw new IllegalArgumentException("You are already a member of this chatroom.");
		}
		// can't join
		if(!canJoin(user, chatroom)) {
			throw new UnauthorizedException("You need an invite to join this chatroom.");
		}
		// delete the member invite, if there is one
		deleteMemberInvitation(user, chatroom);
		// create relation / send the invite
		createMemberRelation(user, chatroom);
	}
	
	/**
	 * check if the user can join a given chatroom
	 * @param user
	 * @param chatroom
	 * @return
	 */
	public boolean canJoin(User user, Chatroom chatroom) {
		// invites are needed to join the chatroom and the useer has not received an invite
		if(chatroom.getInvited_only() && !memberInvitationSent(user, chatroom)) {
			return false;
		}
		return true;
	}
	
	/**
	 * send a member invitation from chatroom to username
	 * @param user
	 * @param chatroom
	 * @throws IllegalArgumentException if user is already member or already has bending invite
	 */
	public void sendMemberInvite(User user, Chatroom chatroom) throws IllegalArgumentException {
		if(isMember(user, chatroom)) {
			throw new IllegalArgumentException("User is already a member of this chatroom");
		}
		if(memberInvitationSent(user, chatroom)) {
			throw new IllegalArgumentException("User already has a pending member request");
		}
		// send member invitation
		createMemberInvitation(user, chatroom);
	}
	
	/**
	 * Check if a user is a member of a chatroom
	 * @param user
	 * @param chatroom
	 * @return true if user is a member, else false
	 */
	public boolean isMember(User user, Chatroom chatroom) {
		List<Chatroom> memberOfRooms = user.getMemberOfChatrooms();
		List<User> members = chatroom.getMembers();
		
		return memberOfRooms.contains(user) && members.contains(chatroom);
	}
	
	/**
	 * Check if a user is an administrator of a chatroom
	 * @param user
	 * @param chatroom
	 * @return true if user is an administrator, else false
	 */
	public boolean isAdmin(User user, Chatroom chatroom) {
		List<Chatroom> adminOfRooms = user.getAdminOfChatrooms();
		List<User> admins = chatroom.getAdministrators();
		
		return adminOfRooms.contains(user) && admins.contains(chatroom);
	}
	
	/**
	 * Check if a user is the owner of a chatroom
	 * @param user
	 * @param chatroom
	 * @return true if user is owner, else false
	 */
	public boolean isOwner(User user, Chatroom chatroom) {
		List<Chatroom> ownedRooms = user.getOwnedChatrooms();
		
		return ownedRooms.contains(chatroom) && chatroom.getOwner() == user;
	}

	/**
	 * Check if a user has received a membership invite to the chatroom
	 * @param user
	 * @param chatroom
	 * @return true if a user has received a membership invite to the chatroom, else false
	 */
	public boolean memberInvitationSent(User user, Chatroom chatroom) {
		List<Chatroom> chatroomInvites = user.getChatroomInvites();
		List<User> invitees = chatroom.getMemberInvitees();
		
		return invitees.contains(user) && chatroomInvites.contains(chatroom);
	}

	/**
	 * Check if a user has received an admin invite to the chatroom
	 * @param user
	 * @param chatroom
	 * @return true if a user has received an admin invite to the chatroom, else false
	 */
	public boolean adminInvitationSent(User user, Chatroom chatroom) {
		List<Chatroom> adminInvites = user.getChatroomAdminInvites();
		List<User> admins = chatroom.getAdminInvitees();
		
		return admins.contains(user) && adminInvites.contains(chatroom);
	}
	
	/**
	 * delete a chatroom (and all its relations
	 * @param chatroom
	 */
	@Transactional(readOnly = false)
	public void deleteChatroom(Chatroom chatroom) {
		chatroomRepository.delete(chatroom);
	}

	/**
	 * Create a member_of relation between user and chatroom
	 * @param user
	 * @param chatroom
	 */
	private void createMemberRelation(User user, Chatroom chatroom) {
		List<Chatroom> memberOfRooms = user.getMemberOfChatrooms();
		List<User> members = chatroom.getMembers();
		
		memberOfRooms.add(chatroom);
		members.add(user);
		// save the chatroom, and its relations
		chatroomRepository.save(chatroom);
	}
	/**
	 * Create a admin_of relation between user and chatroom
	 * @param user
	 * @param chatroom
	 */
	private void createAdminRelation(User user, Chatroom chatroom) {
		List<Chatroom> adminOfRooms = user.getAdminOfChatrooms();
		List<User> admins = chatroom.getAdministrators();
		
		adminOfRooms.add(chatroom);
		admins.add(user);
		// save the chatroom, and its relations
		chatroomRepository.save(chatroom);
	}

	/**
	 * create INVITES relation from chatroom to user
	 * @param user
	 * @param chatroom
	 */
	private void createMemberInvitation(User user, Chatroom chatroom) {
		List<Chatroom> invites = user.getChatroomInvites();
		List<User> invitees = chatroom.getMemberInvitees();
		// create the relation
		invites.add(chatroom);
		invitees.add(user);
		// save the chatroom, and its relations
		chatroomRepository.save(chatroom);
	}
	/**
	 * delete INVITES relation from chatroom to user, if it exists
	 * @param user
	 * @param chatroom
	 */
	private void deleteMemberInvitation(User user, Chatroom chatroom) {
		List<Chatroom> invites = user.getChatroomInvites();
		List<User> invitees = chatroom.getMemberInvitees();
		// if the user has received invite
		if(memberInvitationSent(user, chatroom)) {
			// create the relation
			invites.remove(chatroom);
			invitees.remove(user);
			// save the chatroom, and update its relations
			chatroomRepository.save(chatroom);
		}
	}
	
	// getters fyrir members, admins, owners, og allar gerðir invites
	
	// inv admin, join admin, transfer owner
}
