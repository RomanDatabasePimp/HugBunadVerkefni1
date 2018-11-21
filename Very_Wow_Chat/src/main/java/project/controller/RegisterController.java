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

/* This class handles http POST, PUT for registration  of a new user  */
@RestController
public class RegisterController {


	@Autowired
	private RedisService redisService; // redis services to insert the user into temp storage
	
	@Autowired
	private UserService userService; // connect to neo4j db
	
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
		
		
		HttpResponseBody clientResponse = new HttpResponseBody(); // create a instance of the response body
		// AuthenticationService authenticator = new AuthenticationService(this.userService);// authenticator
																						
		// this is not for the api more of for debuggin.
		if (!payload.allInfoExists()) {
			/*
			 * reason why this is a single Error not a list of errors is cuz this should
			 * never happen heroku should catch this and stop it from sending an empty
			 * resource this is more for catching with testing proposes like postman and  stuff
			 */
			clientResponse.addSingleError("error", "All information must be filled");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
		}

		/*
		 * before starting authenticating we check if the client can have this username
		 * i decided if the username is taken there is no reason to validate rest of the  data
		 */
		if (authenticator.userNameExists(payload.getUserName())) {
			clientResponse.addErrorForForm("Username", "Username already exists");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
		}

		/* now that we know we have all the data we need and we know the username is
		 * legit we can start validating the data  We start calling out validator to 
		 * validate the information and if there is a  problem we take note of it */
		if (!authenticator.passwordsMach(payload.getPassword(), payload.getPasswordReap())) {
			clientResponse.addErrorForForm("Password", "Both passwords must match");
		}
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

		/*
		 * At this point we know that the user data is valid so we proceede creating him
		 * in redis and sending him a validation link our.
		 * Bcryptor for encrypting sensitive data
		 */
		BCryptPasswordEncoder privateInfoEncoder = new BCryptPasswordEncoder();

		// create a json obj that we can store in redis and add all the information to
		// it along with encrypting the private data
		JSONObject newUser = new JSONObject();
		newUser.put("userName", payload.getUserName());
		newUser.put("displayName", payload.getDisplayName());
		newUser.put("password", privateInfoEncoder.encode(payload.getPassword()));
		// reason why we encode email is cuz of the new privacy policies any data that
		// can lead to the user(as a person) has to be secured
		
		
		newUser.put("email", CryptographyService.getCiphertext(payload.getEmail()));

		// insert the data into Redis for 30 min
		this.redisService.insertUser(payload.getUserName(), newUser);
		
		
		
		// the mailMan who will call the webServer to send a validation email
		MailController mailMan = new MailController(payload.getEmail(), payload.getUserName()); // create the mail																								
		mailMan.send(); // send the email to the user

		/* we responde with that the register was successful and dont send any content back */
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);


	}

	/*
	 * Usage : url/validation/{key} 
	 *   For : PUT request key is a String pointer to the data that needs to be validated dosent need to contain any type of json 
	 *  After: checks if the key is points to a unvalidated user, if so it stores the user in neo4j and sets its status as validated
	 */
	@RequestMapping(path = "/validation/{key}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> validateUser(@PathVariable String key) {
		HttpResponseBody clientResponse = new HttpResponseBody(); // create a instance of the response body

		// start by checking if the key exists if it dosent we responde with an not
		if (!this.redisService.userNameExists(key)) {
			clientResponse.addSingleError("error", "User not found or validation period has expired please register again");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.NOT_FOUND);
		}
		
		// now that we know the data exists we fetch it and move it into long term
		JSONObject tempUrs = this.redisService.getAndDestroyData(key); // fetch the data and remove it from json

		// create a new User that will be insert into neo4j
		User newuser = new User(tempUrs.getString("userName"), tempUrs.getString("password"),
				                tempUrs.getString("displayName"), tempUrs.getString("email"));

		// create the user in our neo4j database
	    try {
	    		this.userService.createUser(newuser);    	
	    }catch(HttpException e) {
	        clientResponse.addSingleError("error", e.getMessage());
	        return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
		}
	    
		
		/* we responde with that the validation was successful and dont send any content back */
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

}
