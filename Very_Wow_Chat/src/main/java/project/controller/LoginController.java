package project.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.payloads.HttpResponseBody;
import project.payloads.JwtUser;
import project.persistance.entities.User;
import project.security.JwtGenerator;
import project.services.UserService;

/* This class handles login requested that are posted to the ulr/login  */
@RestController
public class LoginController {

	@Autowired
	private UserService userService; // we need neo4j to auth users
	
	@Autowired
	private JwtGenerator jwtGenerator; // create the JWT token

	/*------------------------------------CONTROLLERS START -----------------------------------------------*/

	/* Usage : url/login 
	 *   For : METHOD TYPE POST Should contain a json obj of a form 
	 *         {"userName":"shitufkc","password":"123123"} 
	 * After : Validates the Client POST request and responds with an appropriate 
	 *         status code along with user data and  a JTW token */
	
	
	/**
	 * 
	 * 
	 * 
	 * <pre class="code">
	 * { "userName": "yourNameHere", "password": "yourPasswordHere" }
	 * </pre>
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> login(@RequestBody JwtUser payload) throws Exception {
		HttpResponseBody clientResponse = new HttpResponseBody(); // create a instance of the response body

		
		// check if the user exists in the neo4j database if dosent exists then we
		// respond with error and 404 not found
		if (!this.userService.userExistsAndActive(payload.getUserName())) {
			clientResponse.addErrorForForm("Username", "Username not found");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.NOT_FOUND);
		}

		// now we know the user exists we fetch him and need to validate the password
		User fetchedUsr = this.userService.findByUsername(payload.getUserName());
		// the password is encrypted in the db so we need to decode it
		BCryptPasswordEncoder privateInfoEncoder = new BCryptPasswordEncoder();
		
		CharSequence raw_password = payload.getPassword();
		

		// check if password matches the requested login
		// if (!privateInfoEncoder.matches(encodedPassword, fetchedUsr.getPassword())) {
		if (!privateInfoEncoder.matches(raw_password, fetchedUsr.getPassword())) {
			clientResponse.addErrorForForm("Password", "Password does not match the username");
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.UNAUTHORIZED);
		}

		/*
		 * create user and jtw to store in session storage This is the obj that will be
		 * stored in the users session storage for now we only need to store his
		 * displayname, username and the JTW for authentication, but later if we need to
		 * store something more its just a couple of adds here and it will work the same
		 */
		JSONObject sessionUsr = new JSONObject();
		sessionUsr.put("username", fetchedUsr.getUsername());
		sessionUsr.put("displayname", fetchedUsr.getDisplayName());
		sessionUsr.put("token", "Token " + this.jwtGenerator.generate(payload));

		clientResponse.addSingleSucc(sessionUsr);// ad the json obj to the response body
		// send user and JTW token back as a succesful response
		return new ResponseEntity<>(clientResponse.getSuccessResponse(), HttpStatus.OK);
	}

}