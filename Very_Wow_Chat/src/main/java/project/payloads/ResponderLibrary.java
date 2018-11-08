package project.payloads;

import java.util.List;
import java.util.stream.Collectors;

import project.persistance.entities.Chatroom;
import project.persistance.entities.Membership;
import project.persistance.entities.User;

public class ResponderLibrary {

	/**
	 * converts a list of Users to a list of UserResponders
	 * @param list
	 * @return
	 */
	public static List<UserResponder> toUserResponderList(List<User> list) {
		return list.stream().map(x -> new UserResponder(x)).collect(Collectors.toList());
	}
	
	/**
	 * converts a list of Chatrooms to a list of ChatroomResponders
	 * @param list
	 * @return
	 */
	public static List<ChatroomResponder> toChatroomResponderList(List<Chatroom> list) {
		return list.stream().map(x -> new ChatroomResponder(x)).collect(Collectors.toList());
	}

	/**
	 * converts a list of Memberships to a list of MembershipResponders
	 * @param list
	 * @return
	 */
	public static List<MembershipResponder> toMembershipResponderList(List<Membership> list) {
		return list.stream().map(x -> new MembershipResponder(x)).collect(Collectors.toList());
	}
}
