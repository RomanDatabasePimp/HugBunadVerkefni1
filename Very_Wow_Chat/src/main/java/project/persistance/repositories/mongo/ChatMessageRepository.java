package project.persistance.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import project.persistance.entities.ChatMessage;

/**
 * An interface for getting chat messages.
 * 
 * @author Davíð Helgason (dah38@hi.is)
 */
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageRepositoryCustom {

}
