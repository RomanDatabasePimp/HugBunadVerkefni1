package project.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import com.mongodb.MongoClient;

import project.persistance.repositories.ChatroomRepository;
import project.persistance.repositories.UserRepository;
import project.persistance.repositories.mongo.ChatMessageRepository;

// @Configuration
// @EnableMongoRepositories(basePackages = "project.persistance.repositories.mongo")
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	public MongoClient mongoClient() {
		return new MongoClient("127.0.0.1", 27017);
		
	}

	@Override
	protected String getDatabaseName() {

		return "test";
	}

}
