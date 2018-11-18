package project;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import project.services.CryptographyService;

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
			String plaintext = "hello";
			String ciphertext = CryptographyService.getCiphertext(plaintext);
			String reconstructedPlaintext = CryptographyService.getPlaintext(ciphertext);
			
			System.out.println(plaintext);
			System.out.println(ciphertext);
			System.out.println(reconstructedPlaintext);
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
