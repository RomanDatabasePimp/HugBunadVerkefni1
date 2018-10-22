package project.persistance.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.EndNode;

import java.util.Date;

@RelationshipEntity(type = "FRIEND_REQUEST")
public class FriendRequest {
	
	@Id @GeneratedValue private Long id;
	
    @StartNode
    private User requestor;

    @EndNode
    private User requestee;
    
    private Date date;
    
}
