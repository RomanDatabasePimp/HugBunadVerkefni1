package project.persistance.repositories;

import java.util.Collection;

import project.persistance.entities.User;
import project.persistance.entities.Friendship;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.neo4j.annotation.Query;

public interface UserRepository extends Neo4jRepository<User, Long>{
	User findByUserName(@Param("userName") String userName);
	
	@Query("MATCH (u:User)-[r:FRIENDS]-(f:User) WHERE u.userName = {userName} return u,r,f;")
	Collection<Friendship> getUserFriends(@Param("userName") String userName);
}
