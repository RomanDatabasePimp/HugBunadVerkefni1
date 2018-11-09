package project.persistance.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.awt.TextArea;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NodeEntity
public class Chatroom {

	@Id @GeneratedValue protected Long id;
	// unique name serving as an identifier
	protected String chatroomName;
	// non-unique name to be displayed
	protected String displayName;
	// description of the chatroom
	protected String description;
	// denotes the visibility of the chatroom: true means listed, false means unlisted
	protected Boolean listed;
	// when the chatroom was created
	protected Long created;
	// timestamp of when the latest message was received
	protected Long lastMessageReceived;
	// denots the accessability of the chatroom: true means users can only join with an invite, false means anyone can join
	protected Boolean invited_only;
	// the owner of the chatroom, has master privileges 
	@Relationship(type="OWNS", direction=Relationship.INCOMING)
	protected User owner;

	// the tags the chatroom is associated with
//	@Relationship(type="HAS_TAG", direction=Relationship.OUTGOING)
//	protected List<Tag> tags;
	
	// users who are members of the chatroom
	@Relationship(type="HAS_MEMBER", direction=Relationship.OUTGOING)
	protected List<User> members;
	
	// users who have administrative privileges of the chatroom
	@Relationship(type="ADMIN_OF", direction=Relationship.INCOMING)
	protected List<User> administrators;

	// users who have been invited to join the chatroom
	@Relationship(type="INVITES", direction=Relationship.OUTGOING)
	protected List<User> memberInvitees;

	// users who have benen invited to become administrators of the chatroom
	@Relationship(type="ADMIN_INVITES", direction=Relationship.OUTGOING)
	protected List<User> adminInvitees;

	// users who have requested to become a member of the chatroom
	@Relationship(type="REQUESTS_TO_JOIN", direction=Relationship.INCOMING)
	protected List<User> requestors;
	
	// Empty constructor required as of Neo4j API 2.0.5
	public Chatroom () {}
	
	/**
	 * Create a new chatroom
	 * @param chatroomName
	 * @param displayName
	 * @param description
	 * @param listed
	 * @param invited_only
	 * @param owner
	 */
	public Chatroom(String chatroomName, String displayName, String description, Boolean listed, Boolean invited_only) {
		this.chatroomName = chatroomName;
		this.displayName = displayName;
		this.description = description;
		this.listed = listed;
		this.invited_only = invited_only;
		
		this.lastMessageReceived = null;
		this.created = (new Date()).getTime(); // current time
	}

	// getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChatroomName() {
		return chatroomName;
	}

	public void setChatroomName(String chatroomName) {
		this.chatroomName = chatroomName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getListed() {
		return listed;
	}

	public void setListed(Boolean listed) {
		this.listed = listed;
	}

	public Boolean getInvited_only() {
		return invited_only;
	}

	public void setInvited_only(Boolean invited_only) {
		this.invited_only = invited_only;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getLastMessageReceived() {
		return lastMessageReceived;
	}

	public void setLastMessageReceived(Long lastMessageReceived) {
		this.lastMessageReceived = lastMessageReceived;
	}


//	public List<Tag> getTags() {
//		return tags;
//	}
//
//	public void setTags(List<Tag> tags) {
//		this.tags = tags;
//	}

	public List<User> getMembers() {
		if(members == null) {
			members = new ArrayList<>();
		}
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public List<User> getAdministrators() {
		if(administrators == null) {
			administrators = new ArrayList<>();
		}
		return administrators;
	}

	public void setAdministrators(List<User> administrators) {
		this.administrators = administrators;
	}

	public List<User> getAdminInvitees() {
		if(adminInvitees == null) {
			adminInvitees = new ArrayList<>();
		}
		return adminInvitees;
	}

	public void setAdminInvitees(List<User> adminInvitees) {
		this.adminInvitees = adminInvitees;
	}

	public List<User> getRequestors() {
		if(requestors == null) {
			requestors = new ArrayList<>();
		}
		return requestors;
	}

	public void setRequestors(List<User> requestors) {
		this.requestors = requestors;
	}

	public List<User> getMemberInvitees() {
		if(memberInvitees == null) {
			memberInvitees = new ArrayList<>();
		}
		return memberInvitees;
	}

	public void setMemberInvitees(List<User> memberInvitees) {
		this.memberInvitees = memberInvitees;
	}
	
}
