package project.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.Errors.NotFoundException;
import project.payloads.HttpResponseBody;
import project.payloads.PasswordResetRequest;
import project.payloads.UserRegistrationFormReceiver;
import project.persistance.entities.User;
import project.pojo.Mailer;
import project.services.AuthenticationService;
import project.services.CryptographyService;
import project.services.RedisService;
import project.services.UserService;

@RestController
public class PasswordResetController {
	
	// TODO: remove later
	private static boolean DEBUG = true;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private UserService userService;
	
	@Value("${email.server.url}")
	private String emailServerUrl;

	@Value("${email.server.secretkey}")
	private String emailServerSecretKey;
	
	// password_reset
	@RequestMapping(value = "/password_reset", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> passwordReset(@RequestBody PasswordResetRequest prr) {
		
		
		// Check a user exists with this email.
		
		try {
			
			String username = prr.getUsername();
			
			if (DEBUG) {
				System.out.println("USERNAME:[" + username + "]");
			}
			
			
			User user = userService.findByUsername(username);
			
			// FIXME: maybe in the future this email might be encrypted so
			// it needs to be decrypted via CryptographyService.
			String recipientEmail = user.getEmail();
			
			String randomKey = CryptographyService.getRandomHexString(64);
			
			redisService.insertString(randomKey, username);
			
			String resetUrl = emailServerUrl + "password_reset/" + randomKey;
			
			
			if (DEBUG) {
				System.out.println("http://localhost" + ":9090" + "/"+ "password_reset/" + randomKey);
			}
			
			
			String emailContent = "Reset URL: " + resetUrl;
			
			Mailer mailer = new Mailer(recipientEmail, emailContent, emailServerUrl, emailServerSecretKey);
			mailer.send();
			
			if (DEBUG) {
				System.out.println(emailContent);
			}
			

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// password_reset
	@RequestMapping(value = "/password_reset/{key}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> passwordResetComplete(@PathVariable String key) {
		try {
			if (!this.redisService.userNameExists(key)) {			
				return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
			}
			
			String username = redisService.getAndDestroyString(key);
			String password = CryptographyService.getStrongRandomPassword(20);
			
			if (DEBUG) {
				System.out.println("Password: " + password);
			}
			
			
			userService.updateUser(username, null, null, password);
			
			JSONObject obj = new JSONObject();
			obj.put("password", password);
			
			return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>("not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
