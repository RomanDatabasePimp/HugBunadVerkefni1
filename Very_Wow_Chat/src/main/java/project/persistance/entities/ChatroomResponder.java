package project.persistance.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * This class is for wrapping data in json objects
 * @author Vilhelm
 *
 */
public class ChatroomResponder {
	// unique name serving as an identifier
	private String chatroomName;
	// non-unique name to be displayed
	private String displayName;
	// description of the chatroom
	private String description;
	// denotes the visibility of the chatroom: true means listed, false means unlisted
	private Boolean listed;
	// denots the accessability of the chatroom: true means users can only join with an invite, false means anyone can join
	private Boolean invited_only;
	// the username of the owner of the chatroom
	private String ownerUsername;
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
		this.ownerUsername = chatroom.getOwner() != null ? chatroom.getOwner().getUsername() : "";
		this.created = chatroom.getCreated();
	}
	
	/**
	 * constructor notað af spring controller til að vinna með json objects
	 * @param chatroomName
	 * @param displayName
	 * @param description
	 * @param listed
	 * @param invited_only
	 */
	public ChatroomResponder(String chatroomName, String displayName, String description, Boolean listed, Boolean invited_only) {
		this.chatroomName = chatroomName;
		this.displayName = displayName;
		this.description = description;
		this.listed = listed;
		this.invited_only = invited_only;
	}
	
	/**
	 * converts a list of Chatrooms to a list of ChatroomResponders
	 * @param list
	 * @return
	 */
	public static List<ChatroomResponder> toResponderList(List<Chatroom> list) {
		return list.stream().map(x -> new ChatroomResponder(x)).collect(Collectors.toList());
	}

	
	/**
	 * wrap the response
	 * @return wrapped response
	 */
	public Object wrapResponse() {
		Map<String, ChatroomResponder> wrapper = new HashMap<>();
		wrapper.put("GoodResp", this);
		return wrapper;
	}
	
	// getters

	public String getChatroomName() {
		return chatroomName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getListed() {
		return listed;
	}

	public Boolean getInvited_only() {
		return invited_only;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public Long getCreated() {
		return created;
	}
	
}
