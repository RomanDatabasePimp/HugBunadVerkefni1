package project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.persistance.entities.HttpResponseBody;
import project.persistance.entities.LoginFormReciver;
import project.persistance.entities.UserRegistrationFormReceiver;
import project.services.AuthenticationService;

/* This class handles http POST request on url/register with a sent json obj  */
/*  https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html */
@RestController
public class RegisterController {
  // this contains the clientResponse
  HttpResponseBody clientResponse = new HttpResponseBody();
  // get our authenticator to authenticate the data 
  AuthenticationService authenticator = new AuthenticationService();
  
  /* Usage : url/register
   *  For  : POST request
   *         Should contain a json obj of a form {"userName":"shitufkc",
   *         									  "displayName":"shitCUCK",
   *                                              "password":"123123",
   *                                              "passwordReap":"123123",
   *                                              "email":"shitufck@gmail.com"}
   * After : Validates the Client POST request and responds with an appropriate status code along with data */
  // value is endpoint , mothen is to what method this response to , headders denotes what are we returning
  @RequestMapping(value="/register", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<String> register(@RequestBody UserRegistrationFormReceiver payload) throws Exception {
	// check if the payload contains everything we need to proccess this request if it dosent we respond accordingly
    if(!payload.allInfoExists()) {
     // this.clientResponse.addError("error", "All fields must be filled !");
      return new ResponseEntity<>(this.clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
    // before starting authenticating we check if the client can have this username 
    if(!this.authenticator.userNameExists(payload.getUserName())) {
       //this.clientResponse.addError("error", "Username already exists");
       return new ResponseEntity<>(this.clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
    // now that we know we have all the data we need and we know the username is legit we can start validating the data
    
	return null;
	
  }
}
