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

	// need to have a connection to the database to fetch user that is requested
	@Autowired
	private UserService userService;
	
	// used for creating jwt tokens if the authentication is successful
	@Autowired
	private JwtGenerator jwtGenerator;

	/*------------------------------------CONTROLLERS START -----------------------------------------------*/

	
	/**
	 * Usage : url/login 
	 *   For : METHOD TYPE POST Should contain a json obj of a form 
	 *         {"userName":"name","password":"123123"} 
	 * After : Validates the Client POST request and responds with an appropriate 
	 *         status code along with user data and  a JTW token
	 * 
	 * <pre>
	 * { "userName": "yourNameHere", "password": "yourPasswordHere" }
	 * </pre>
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> login(@RequestBody JwtUser payload) throws Exception {
		/* Since this is a restfull controller, we have our own custom way to respond to the user
		 *  so our responses would be uniformed, this will make it easier to work with in the client side */
		HttpResponseBody clientResponse = new HttpResponseBody();
		
		/* The initial idea was, when the controllers are called, they call different services to 
		 *  Fulfill the request that was asked of them, the services return either errors or data that was requested
		 *  the controller collects these errors from all the services and makes a response out of them 
		 *  or sends the data if the request was successful */
	
		/* check if the user exists in the database if dosen't exists then we
		    respond with error along with 404(not found) HTTP response */
		if (!this.userService.userExistsAndActive(payload.getUserName())) {
			// create a error
			clientResponse.addErrorForForm("Username", "Username not found");
			// return the error
			return new ResponseEntity<>(clientResponse.getErrorResponse(), HttpStatus.NOT_FOUND);
		}

		// at this point we know the user exists we fetch him and then we need to validate the password
		User fetchedUsr = this.userService.findByUsername(payload.getUserName());
		/* the password is hashed in the database so we need a way to authenticate
		 * and confirmed that the given password from the client is correct. */
		BCryptPasswordEncoder privateInfoEncoder = new BCryptPasswordEncoder();
		// fetch the raw password from the client input
		CharSequence raw_password = payload.getPassword();

		/* check if password matches the requested login,
		 * if the password dosent match we create an error and responde with it */
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