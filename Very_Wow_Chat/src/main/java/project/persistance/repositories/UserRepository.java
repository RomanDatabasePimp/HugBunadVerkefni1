package project.persistance.repositories;

import java.util.Collection;

import project.persistance.entities.User;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.annotation.Query;

/**
 * Repository interface for neo4j and users
 * @author Vilhelml
 *
 */
public interface UserRepository extends Neo4jRepository<User, Long>{
	// Return a User NodeEntity if userName exists
	User findByUsername(@Param("username") String username);
	
	// save a user in databse, for creates and updates
	User save(User user);

	// delete a user
	void delete(User user);
}
