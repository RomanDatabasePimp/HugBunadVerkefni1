package project.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.persistance.entities.HttpResponseBody;
import project.persistance.entities.UserRegistrationFormReceiver;
import project.services.AuthenticationService;
import project.services.RedisService;

/* This class handles http POST request on url/register with a sent json obj  */
/* https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html */
@RestController
public class RegisterController {
  // this contains the clientResponse
  private HttpResponseBody clientResponse;
  // get our authenticator to authenticate the data 
  private AuthenticationService authenticator = new AuthenticationService();
  // our Bcryptor for the sensitive data
  private BCryptPasswordEncoder privateInfoEncoder = new BCryptPasswordEncoder();
  private MailController mailMan;
  private RedisService redisService = new RedisService();
  
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
	this.clientResponse = new HttpResponseBody(); // create a new instance of the response body
	
	// check if the payload contains everything we need to proccess this request if it dosent we respond accordingly
    if(!payload.allInfoExists()) {
      // reason why this is a single Error not a list of errors is cuz this should never happen heroku should catch
      // this and stop it from sending an empty resource this is more for catching with testing porpuses like postman and stuff
      this.clientResponse.addSingleError("error","All information must be filled");
      return new ResponseEntity<>(this.clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
    
    /* before starting authenticating we check if the client can have this username
       i decided if the username is taken there is no reason to validate the other sets of given data */
    if(this.authenticator.userNameExists(payload.getUserName())) {
       this.clientResponse.addToErrorArray("errors", "username", "Username already exists");
       return new ResponseEntity<>(this.clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
    
    // now that we know we have all the data we need and we know the username is legit we can start validating the data
    if(!this.authenticator.passwordsMach(payload.getPassword(), payload.getPasswordReap())) {
      this.clientResponse.addToErrorArray("errors", "password", "Both passwords must match");
    }
    if(!this.authenticator.validatePass(payload.getPassword())) {
      this.clientResponse.addToErrorArray("errors", "password", "Password must be  atleast 8 characters long"
      		+ "have 1 digit,lower,uppercase,specialcase letter and cannot contain spaces/tabs");
    }
    // if errors exists then we return the errors as the response
    if(this.clientResponse.errorsExist()) {
      return new ResponseEntity<>(this.clientResponse.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
    
    /* At this point we know that the user data is valid so we proceede creating him in redis and sending him a validation link */
    // create a json obj that we can store in redis and add all the information to it along with encrypting the private data
    JSONObject newUser = new JSONObject();
    newUser.put("userName", payload.getUserName());
    newUser.put("displayName", payload.getDisplayName());
    newUser.put("password", this.privateInfoEncoder.encode(payload.getPassword()));
    // reason why we encode email is cuz of the new privacy policies any data that can lead to the user(as a person) has to be secured
    newUser.put("email", this.privateInfoEncoder.encode(payload.getEmail()));
    
    // insert the data into Redis for 30 min
    this.redisService.insertUser(payload.getUserName(), newUser);
    
    // now we can send email to our new user
    this.mailMan = new MailController(payload.getEmail(),payload.getUserName()); // create the mail package
    this.mailMan.send(); // send the email to the user 
    
    /* Responde finnally with a happy message that the user needs to validate his account before he can use it*/
    this.clientResponse.addSingleSucc("success","Registration successful, a  validation link has been send to :"+payload.getEmail());
    return new ResponseEntity<>(this.clientResponse.getSuccessResponse(), HttpStatus.CREATED);
	
  }
}
