package project.persistance.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.EndNode;

import java.util.Date;

@RelationshipEntity(type = "FRIENDS")
public class Friendship {
	
	@Id @GeneratedValue private Long id;
	
    @StartNode
    private User user1;

    @EndNode
    private User user2;
    
    private Date date;
    
    public Friendship() {}
    
    public Friendship(User user1, User user2) {
		this.user1 = user1;
		this.user2 = user2;
    }

	public Long getId() {
		return id;
	}

	public User getUser1() {
		return user1;
	}

	public User getUser2() {
		return user2;
	}

	public Date getDate() {
		return date;
	}
    
	/**
	 * @description 	Given a user of a friendship, return the other user
	 * @param user		User whose friend will be returned
	 * @return			the friend of the given user of a friendship
	 */
    public User getOtherUser(User user) {
    	if(user == this.user1) {
    		return this.user2;
    	}
    	if(user == this.user2) {
    		return this.user1;
    	}
    	return null;
    	
    }
}
