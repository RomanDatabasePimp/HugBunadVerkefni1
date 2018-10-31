package project.controller;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.persistance.entities.HttpResponseBody;
import project.persistance.entities.LoginFormReciver;

/* This class handles http POST request on url/login with a sent json obj  */
/*  https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html */
@RestController
public class LoginController {
<<<<<<< HEAD
	private HttpReturner clientResponse = new HttpReturner(); // responsible for creating json response objects

	/*
	 * Usage : url/login For : METHOD TYPE POST Should contain a json obj of a form
	 * {"username":"shitufkc","password":"123123"} After : Validates the Client POST
	 * request and responds with an appropriate status code along with data
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> login(@RequestBody LoginFormReciver payload) throws Exception {
		ArrayList<String> errors = new ArrayList<String>();// Array List that will contain all the errors
		/*
		 * If an enmpty send was somehow made we will handle it we resonde with a 400
		 * status code along with json error
		 */
		if (payload.getPassword() == null || payload.getUsername() == null) {
			errors.add("Illegal json format recived. leagal format should only have username and password ");
			this.clientResponse.createErrors(errors);
			return new ResponseEntity<>(this.clientResponse.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(payload.getUsername());
		
		/*
		 * If username is not found in the services then We responde with a 404 error
		 * along with a json error
		 */
		if (!UserNameEx(payload.getUsername())) {
			errors.add("Username not found");
			this.clientResponse.createErrors(errors);
			return new ResponseEntity<>(this.clientResponse.getErrors(), HttpStatus.NOT_FOUND);
		}
		/*
		 * if the username exists then the next step is to check if the password matches
		 * sed username if it does not then we responde with status code of 401 and json
		 * erro
		 */
		if (!PassEx(payload.getUsername(), payload.getPassword())) {
			errors.add("Password did not match");
			this.clientResponse.createErrors(errors);
			return new ResponseEntity<>(this.clientResponse.getErrors(), HttpStatus.UNAUTHORIZED);
		}
		
		// TODO: figure out what to do if we find the user.

		return null;
	}


	/**
	 * 
	 * DH: I think this method checks whether user `Usr` exists.
	 * 
	 * While db con is not rdy this is in place
	 * @param Usr
	 * @return
	 */
	public boolean UserNameEx(String Usr) {
		return true;
	}

	public boolean PassEx(String Usr, String password) {
		return true;
	}

=======
   HttpResponseBody ddd = new HttpResponseBody();
  /* Usage : url/login 
   * For   : METHOD TYPE POST
   *         Should contain a json obj of a form {"username":"shitufkc","password":"123123"}
   * After : Validates the Client POST request and responds with an appropriate status code along with data */
  @RequestMapping(value="/login", method = RequestMethod.GET,headers = "Accept=application/json")
  public ResponseEntity<String> login() throws Exception {
	this.ddd.addToSuccArray("errors", "username","fick");
    return new ResponseEntity<>(this.ddd.getSuccessResponse(), HttpStatus.OK);
  }
  
  /* While db con is not rdy this is in place */
  public boolean UserNameEx(String Usr) {
	  return true;
  }
  public boolean PassEx(String Usr, String password) {
	  return true;
  }
 
  
>>>>>>> 19385cde6581dff0eb9c3d83daf2eccdccf76f5a
}