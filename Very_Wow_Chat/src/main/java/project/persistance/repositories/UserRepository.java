package project.persistance.repositories;

import java.util.Collection;

import project.persistance.entities.User;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.annotation.Query;

public interface UserRepository extends Neo4jRepository<User, Long>{
	// Return a User NodeEntity if userName exists
	User findByUserName(@Param("userName") String userName);
	
	User save(User user);
	
}
