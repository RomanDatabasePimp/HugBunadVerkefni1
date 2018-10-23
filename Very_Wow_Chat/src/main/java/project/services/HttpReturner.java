package project.services;

import java.util.ArrayList;

import org.json.JSONObject;

/* Http Returner is responsible ofr creating and returning 
 * appropriate json objects to the client as responses  */
public class HttpReturner {
	
  private JSONObject errors;
  private JSONObject loginSucc; 
  
  /* We dont want to initiate anything since many classes will call HtttpReturner
   * and not all of them will use all of these jsonobjs */
  public HttpReturner() {}
  
  public void createUserSucc() {
	  
  } 
  
  public void createErrors(ArrayList<String> errs) {
	this.errors = new JSONObject();
    for(String err:errs) {
      JSONObject singleErr = new JSONObject();
	  this.errors.append("errors",singleErr.put("error", err));
	}
  }
  
  public String getErrors() { return this.errors.toString();}
  
  
}
