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
	
	@Autowired
	private CryptographyService cryptographyService;
	
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
		
		if (false) {
			
			System.out.println("Generated salt: " + cryptographyService.generateSalt());
			
			
			String originalPlaintext = "This is super secret!";
			String ciphertext = cryptographyService.getCiphertext(originalPlaintext);
			String reconstructedPlaintext = cryptographyService.getPlaintext(ciphertext);
			
			System.out.println("Original plaintext: " + originalPlaintext);
			System.out.println("Ciphertext: " + ciphertext);
			System.out.println("Reconstructed plaintext: " + reconstructedPlaintext);
		}
	}
}
