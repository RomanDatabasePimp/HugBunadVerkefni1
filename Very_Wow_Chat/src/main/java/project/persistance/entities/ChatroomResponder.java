package project.persistance.entities;

import java.awt.TextArea;

public class ChatroomResponder {
	// unique name serving as an identifier
	private String chatroomName;
	// non-unique name to be displayed
	private String displayName;
	// description of the chatroom
	private TextArea description;
	// denotes the visibility of the chatroom: true means listed, false means unlisted
	private Boolean listed;
	// denots the accessability of the chatroom: true means users can only join with an invite, false means anyone can join
	private Boolean invited_only;
	// the owner of the chatroom, has master privileges 
	private User owner;
	// when the chatroom was created
	private Long created;
	
	/**
	 * Create a responder from a chatroom
	 * @param chatroom chatroom to be transformed
	 */
	public ChatroomResponder(Chatroom chatroom) {
		this.chatroomName = chatroom.getChatroomName();
		this.displayName = chatroom.getDisplayName();
		this.description = chatroom.getDescription();
		this.listed = chatroom.getListed();
		this.invited_only = chatroom.getInvited_only();
		this.owner = chatroom.getOwner();
		this.created = chatroom.getCreated();
	}

	public String getChatroomName() {
		return chatroomName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public TextArea getDescription() {
		return description;
	}

	public Boolean getListed() {
		return listed;
	}

	public Boolean getInvited_only() {
		return invited_only;
	}

	public User getOwner() {
		return owner;
	}

	public Long getCreated() {
		return created;
	}
	
}
