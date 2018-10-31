package project.persistance.repositories;

import java.util.Collection;

import project.persistance.entities.Chatroom;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.annotation.Query;

/**
 * Repository interface for neo4j and chatrooms
 * @author Vilhelml
 *
 */
public interface ChatroomRepository extends Neo4jRepository<Chatroom, Long>{
	// Return a User NodeEntity if userName exists
	Chatroom findByChatroomName(@Param("chatroomName") String chatroomName);
	// create new chatroom in datbase
	Chatroom save(Chatroom chatroom);
	// delete a chatroom
	void delete(Chatroom chatroom);
}
