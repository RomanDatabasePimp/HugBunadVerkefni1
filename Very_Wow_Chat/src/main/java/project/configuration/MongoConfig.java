package project.configuration;



import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.MongoClient;

public class MongoConfig {}

// @Configuration
// @EnableMongoRepositories(basePackages = "project.persistance.repositories.mongo")

/*public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	public MongoClient mongoClient() {
		return new MongoClient("127.0.0.1", 27017);
		
	}

	@Override
	protected String getDatabaseName() {

		
		
		return "test";
	}

}
*/