package project.persistance.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import project.persistance.entities.Tag;

public interface TagRepository extends Neo4jRepository<Tag, Long> {

	/**
	 * Return a Tag NodeEntity if tagName exists.
	 */
	Tag findByName(@Param("tagName") String tagName);

	/**
	 * Get all chat rooms (not tags?).
	 */
	List<Tag> findAll();

	/**
	 * Create a new Tag in database.
	 */
	Tag save(Tag tag);

	/**
	 * Delete a Tag.
	 */
	void delete(Tag tag);

	/**
	 * Delete all nodes that have no relations.
	 */
	@Query("MATCH (n:Tag) WHERE size((n)--())=0 DELETE n;")
	void deleteTagsWithNoRelations();
}
