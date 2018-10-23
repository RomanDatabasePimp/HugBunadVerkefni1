package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.persistance.entities.LoginFormReciver;
import project.services.HttpReturner;
import project.services.RegisterFormReciver;

/* This class handles http POST request on url/register with a sent json obj  */
/*  https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html */
@RestController
public class RegisterController {
  private HttpReturner clientResponse = new HttpReturner();  // responsible for creating json response objects
  
  /* Usage : url/register
   *  For  : POST request
   *         Should contain a json obj of a form {"username":"shitufkc",
   *                                              "password":"123123",
   *                                              "passwordReapeat":"123123",
   *                                              "email":"shitufck@gmail.com"}
   * After : Validates the Client POST request and responds with an appropriate status code along with data */
  @RequestMapping(value="/register", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<String> register(@RequestBody RegisterFormReciver payload) throws Exception {
	  System.out.println(payload.validateEmail());
	return null;
	
	  
  }
}
