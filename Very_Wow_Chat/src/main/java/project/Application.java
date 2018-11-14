package project;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

import project.persistance.entities.ChatMessage;
import project.persistance.repositories.mongo.ChatMessageRepository;

/**
 * This is the main class of our whole Project you need to run this class to
 * start the Spring boot App
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
	
	// Logger
	
	private static final Log log = LogFactory.getLog(Application.class);
	
	// So we have access to application context (for test purposes)
	@Autowired
	private ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		if (true) {
			// Some MongoDB tests
			System.out.println("Hello world!");
			
			System.out.println(applicationContext);
			
			ChatMessageRepository cmr = applicationContext.getBean(ChatMessageRepository.class);
			
			System.out.println(cmr);
			
			List<ChatMessage> a = cmr.findAll();
			for (ChatMessage m : a) {
				System.out.println(m);
			}
		}
		
		if (true) {
			MongoOperations mongoOps = new MongoTemplate(new MongoClient(), "test");
			
			// ChatMessage cm = new ChatMessage();
			// cm.setMessage("yo");
			
			
			// mongoOps.insert(cm);
		}
		
		if (true) {
			// Get defined beans
			
			String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
			
			Arrays.sort(beanDefinitionNames);
			
			System.out.println("Defined beans");
			for (int i = 0; i < beanDefinitionNames.length; i += 1) {
				String beanDefinitionName = beanDefinitionNames[i];
				System.out.println("> (" + (i + 1) + "/" + beanDefinitionNames.length + "): " + beanDefinitionName);
			}
			// Get defined beans
			for (String beanDefinitionName : beanDefinitionNames) {
				
			}
		}
	}
}
