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
   HttpResponseBody ddd = new HttpResponseBody();
  /* Usage : url/login 
   * For   : METHOD TYPE POST
   *         Should contain a json obj of a form {"username":"shitufkc","password":"123123"}
   * After : Validates the Client POST request and responds with an appropriate status code along with data */
  @RequestMapping(value="/login", method = RequestMethod.GET,headers = "Accept=application/json")
  public ResponseEntity<String> login() throws Exception {
	this.ddd.addToErrorArray("errors", "username","fick");
    return new ResponseEntity<>(this.ddd.getErrorResponse(), HttpStatus.BAD_REQUEST);
  }
  
  /* While db con is not rdy this is in place */
  public boolean UserNameEx(String Usr) {
	  return true;
  }
  public boolean PassEx(String Usr, String password) {
	  return true;
  }
 
  
}