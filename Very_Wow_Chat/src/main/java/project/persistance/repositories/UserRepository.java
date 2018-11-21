package project.persistance.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import project.persistance.entities.User;

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
	// delete user relations
	@Query("MATCH (a:User)-[r]-(b) WHERE a.username = \"vilhelml\" DELETE r;")
	void deleteUserRelations(@Param("username") String username);
}
