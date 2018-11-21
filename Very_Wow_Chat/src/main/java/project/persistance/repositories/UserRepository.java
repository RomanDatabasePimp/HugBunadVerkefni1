package project.persistance.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import project.persistance.entities.User;

/**
 * Repository interface for neo4j and users
 * 
 * @author Vilhelml
 */
public interface UserRepository extends Neo4jRepository<User, Long> {

	/**
	 * Returns a <code>User</code> <code>NodeEntity</code> if user with
	 * user name <code>userName</code> exists.
	 * 
	 * @param username Name of user.
	 * @return Returns a User NodeEntity if user with user name userName exists.
	 */
	User findByUsername(@Param("username") String username);

	/**
	 * Save a user in database, for creates and updates.
	 */
	User save(User user);

	/**
	 * Delete a user.
	 */
	void delete(User user);

	/**
	 * Delete user relations.
	 * 
	 * @param username
	 */
	@Query("MATCH (a:User)-[r]-(b) WHERE a.username = \"vilhelml\" DELETE r;")
	void deleteUserRelations(@Param("username") String username);
}
