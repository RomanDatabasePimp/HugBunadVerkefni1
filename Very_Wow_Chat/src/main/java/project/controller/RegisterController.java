package project.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.errors.HttpException;
import project.payloads.HttpResponseBody;
import project.payloads.UserRegistrationFormReceiver;
import project.persistance.entities.User;
import project.services.AuthenticationService;
import project.services.CryptographyService;
import project.services.RedisService;
import project.services.UserService;

/* This class handles http POST, PUT for registration and validation of a new user  */
@RestController
public class RegisterController {

	/**
	 * Temporary storage database, holds the users that still have
	 * to be validated, before beeing added into the long term storage */
	@Autowired
	private RedisService redisService;
	
	/**
	 * Our long term storage database used to store our user after 
	 * he has been validated */
	@Autowired
	private UserService userService;
	
	/**
	 * This class holds over all the basic functions needed to authenticate
	 * or validate data recived from user */
	@Autowired
	private AuthenticationService authenticator;
	


	/*--------------------------------------------CONTROLLERS START---------------------------------------------------------*/

	
	/**
	 * Usage: url/regiseter
	 * 
	 * NOTE: POST request should contain a JSON object of the form
	 * <pre>
	 * {
	 * 	   "userName": "john",
	 * 	   "displayName": "gladwell",
	 *     "password": "AveryLong$ecurePassword123",
	 *     "passwordReap": "AveryLong$ecurePassword123",
	 *     "email": "john.gladwell@gmail.com" 
	 * }
	 * </pre>
	 * 
	 * Validates the client's POST request and responds with an appropriate 
	 * status code along with the data.
	 * 
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> register(@RequestBody UserRegistrationFormReceiver payload) throws Exception {
		/* Since this is a restfull controller, we have our own custom way to respond to the user
		 *  so our responses would be uniformed, this will make it easier to work with in the client side */
		HttpResponseBody clientResponse = new HttpResponseBody();
																						
		/* This is for debuggin, i had problems sometimes when i used Postman where it would not send the 
		 * correct json to the server resulting in wierd errors. Its fine now but we might as well keep it */
		if (!payload.allInfoExists()) {
			/* reason why this is a single Error not a list of errors is cuz this should
			 * never happen heroku should catch this and stop it from sending an empty
			 * resource this is more for catching with testing proposes like postman and  stuff  */
			clientResponse.addSingleError("error", "All information must be filled");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
		}

		/*  before starting authenticating we check if the client can have this username
		 *  i decided if the username is taken there is no reason to validate rest of the  data  */
		if (authenticator.userNameExists(payload.getUserName())) {
			clientResponse.addErrorForForm("Username", "Username already exists");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
		}

		/* now that we know we have all the data we need and we know the username is
		 * legit we can start validating the data  We start by calling the validator to 
		 * validate the information and if there is a  problem we take note of it */
		if (!authenticator.passwordsMach(payload.getPassword(), payload.getPasswordReap())) {
			clientResponse.addErrorForForm("Password", "Both passwords must match");
		}
		/* We can split this into individual errors but that would just make the code 
		 * bigger and messier also for the validator same thing would be applied.
		 * so insted we have one big password strenght check and if it fails we
		 * send a list of things that have to be corret.*/
		if (!authenticator.validatePass(payload.getPassword())) {
			clientResponse.addErrorForForm("Password", "Must be atleast 8 characters long");
			clientResponse.addErrorForForm("Password", "Must contain a upper and a lowercase letter");
			clientResponse.addErrorForForm("Password", "Must contain a specialcase letter");
			clientResponse.addErrorForForm("Password", "Cannot contain spaces or tabs");
		}
		if (!authenticator.validEmail(payload.getEmail())) {
			clientResponse.addErrorForForm("Email", "Email must be of valid form");
		}

		// if errors exists then we return the errors as the response
		if (clientResponse.errorsExist()) {
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
		}

		/* At this point we know that the user data is valid so we proceed creating the user
		 * in our short term storage and sending him a validation link.
		 * Bcryptor for hashing sensitive data */
		BCryptPasswordEncoder privateInfoEncoder = new BCryptPasswordEncoder();

		/* create a json obj that we can store in the short term database 
		 * and add all the information to it along with encrypting the private data */
		JSONObject newUser = new JSONObject();
		newUser.put("userName", payload.getUserName());
		newUser.put("displayName", payload.getDisplayName());
		// NOTE: password is hashed here.
		newUser.put("password", privateInfoEncoder.encode(payload.getPassword()));
		/* the reason why we encode email is cuz of the new privacy policies any data that
		   can lead to the user(as a person) has to be secured */
		
		// NOTE: email is encrypted here.
		newUser.put("email", CryptographyService.getCiphertext(payload.getEmail()));

		/* Please NOTE hashing and encrypting are not the samething, in short
		 * if you hash you can never get the raw data you can compare and see if it matches
		 * encrypted data can be decrypted to see the its raw form please be carefull 
		 * when choosing how to hide sensitive information */ 
		
		/* Store the data in the short term storage, in the short term storage its defined
		 * how long is the period of time its stored until its deleted */
		this.redisService.insertUser(payload.getUserName(), newUser);
		
		// the mailMan who will call the webServer to send a validation email
		MailController mailMan = new MailController(payload.getEmail(), payload.getUserName()); // create the mail																								
		mailMan.send(); // send the email to the user

		// we responde with that the register was successful and dont send any content back
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Usage:  url/validation/{key} 
	 * 
	 * <pre>
	 *  Usage : url/validation/{key} 
	 *    For : PUT request key is a String pointer to the data that needs to be validated dosent need to contain any type of json 
	 *   After: checks if the key is points to a unvalidated user, if so it stores the user in neo4j and sets its status as validated
	 * </pre>
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/validation/{key}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> validateUser(@PathVariable String key) {
		// the explanation for this is above in the login controller line : 74 
		HttpResponseBody clientResponse = new HttpResponseBody();
		
		/* We check if the key exists in our short term storage */
		if (!this.redisService.userNameExists(key)) {
			clientResponse.addSingleError("error", "User not found or validation period has expired please register again");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.NOT_FOUND);
		}
		
		// NOTE that we know the data exists we fetch it and move it into long term
		/* NOTE: we assume the email of this JSON object is encrypted 
		   (from registration everything that needs to be hashed/encrypted should be hashed/encrypted).*/
		JSONObject tempUrs = this.redisService.getAndDestroyData(key); // fetch the data and remove the data from shortterm storage

		// create a new User that will be insert into our long term storage
		User newuser = new User(tempUrs.getString("userName"), tempUrs.getString("password"),
				                tempUrs.getString("displayName"), tempUrs.getString("email"));

		// create the user in our long term database
	    try {
	    		this.userService.createUser(newuser);    	
	    }catch(HttpException e) {
	        clientResponse.addSingleError("error", e.getMessage());
	        return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
		}
	    	
		// we responde with that the validation was successful and dont send any content back
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

}
