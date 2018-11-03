package project.persistance.entities;

import java.util.ArrayList;

import org.json.JSONObject;

/* This is the HTTP response format we will use i.e many Controllers can return many different things
 * and its important to make some kind of unified way of returning our results to the client.
 * This will create less  problems when we will implement the api-communicator in the webApp  */
public class HttpResponseBody {

	/*
	 * We want to have somekind of way to denote right away if the response is a
	 * successful or not when u get a response it will bed wrapped in BadResp or
	 * GoodResp this way in the api-communicator this style along with Http status
	 * codes gives us a very good way of handling responses late down the line
	 */
	private JSONObject errors_response;
	private JSONObject successful_response;

	/*
	 * A response can consist of many thing this Array can store many individual
	 * things so maybe if u only want to send one response it is possible
	 */
	private ArrayList<JSONObject> error; // container for errors
	private ArrayList<JSONObject> response; // container for successes

	/*
	 * Sometimes you want to return a obj that has a list of objects that denote
	 * different things, this is also possible !
	 */
	private JSONObject many_in_one_erros; // container for erros
	private JSONObject many_in_one_succ; // contriner for successes

	public HttpResponseBody() {
		this.errors_response = new JSONObject();
		this.successful_response = new JSONObject();
		this.error = new ArrayList<JSONObject>();
		this.response = new ArrayList<JSONObject>();
		this.many_in_one_erros = new JSONObject();
		this.many_in_one_succ = new JSONObject();
	}

	/*-----------------------------------------------------------------------------*/
	/*------------------------------ERROR SETTERS START----------------------------*/
	/*-----------------------------------------------------------------------------*/

	/*
	 * Usage : addErrorForForm(FieldVal,Message) 
	 *   For : FieldVal - String input field type (username , password ,email etc..) 
	 *   	   Message - String error msg for the field 
	 *  After: creates a errors obj that holds many error msg each beeing {field:Fieldtype, message:message}
	 */
	public void addErrorForForm(String fieldVal, String message) {
		JSONObject newobj = new JSONObject();
		newobj.put("field", fieldVal);
		newobj.put("message", message);
		this.many_in_one_erros.append("errors", newobj);
	}

	/*
	 * Usage : addToErrorArray(arrayKey,key,value) 
	 *   For : arrayKey(STRING) - ID of the array you want to add this into 
	 *         key(STRING) - key of the json obj that will be created 
	 *         value - value of key 
	 *  After: adds the a new json obj {key:value} to the arrayKey list
	 */
	public void addToErrorArray(String arrayKey, String key, String value) {
		JSONObject newobj = new JSONObject();
		newobj.put(key, value);
		this.many_in_one_erros.append(arrayKey, newobj);
	}

	/*
	 * YES OVERLOAD !!!! so you can even insert your own custom made jsonobject omg i am so hard !
	 */

	/*
	 * Usage : addToErrorArray(arrayKey,newobj) 
	 *   For : arrayKey(STRING) - ID of the array you want to add this into newobj(JSONObject)- just a jsonobject 
	 *  After: adds the a new json obj to the arrayKey list
	 */
	public void addToErrorArray(String arrayKey, JSONObject newobj) {
		this.many_in_one_erros.append(arrayKey, newobj);
	}

	/*
	 * Usage : addSingleError(key,value) 
	 *   For : key,value are strings 
	 *  After: adds a  single error to the error List
	 */
	public void addSingleError(String key, String value) {
		JSONObject error = new JSONObject();
		this.error.add(error.put(key, value));
	}

	/*
	 * Usage : addSingleError(newobj) 
	 *   For : newobj is a custom JSONObject 
	 *  After: adds a single jsonobj to the error List
	 */
	public void addSingleError(JSONObject newobj) {
		this.error.add(newobj);
	}

	/*-----------------------------------------------------------------------------*/
	/*------------------------------ERROR SETTERS END------------------------------*/
	/*-----------------------------------------------------------------------------*/

	/*-----------------------------------------------------------------------------*/
	/*---------------------------SUCCESS SETTER START------------------------------*/
	/*-----------------------------------------------------------------------------*/

	/*
	 * Usage : addToSuccArray(arrayKey,key,value) 
	 *   For : arrayKey(STRING) - ID of the array you want to add this into 
	 *         key(STRING) - key of the json obj that will  be created value - value of key 
	 *  After: adds the a new json obj {key:value} to the arrayKey list
	 */
	public void addToSuccArray(String arrayKey, String key, String value) {
		JSONObject newobj = new JSONObject();
		newobj.put(key, value);
		this.many_in_one_succ.append(arrayKey, newobj);
	}

	/*
	 * Usage : addToSuccArray(arrayKey,newobj) 
	 *   For : arrayKey(STRING) - ID of the array you want to add this into newobj(JSONObject)
	 *  After: adds the a new json obj to the arrayKey list
	 */
	public void addToSuccArray(String arrayKey, JSONObject newobj) {
		this.many_in_one_succ.append(arrayKey, newobj);
	}

	/*
	 * Usage : addSingleSucc(key,value) 
	 *   For : key,value are strings 
	 * After: adds a single succ to the succ List
	 */
	public void addSingleSucc(String key, String value) {
		JSONObject error = new JSONObject();
		this.response.add(error.put(key, value));
	}

	/*
	 * Usage : addSingleSucc(newobj) 
	 *   For : newobj is a custom JSONObject 
	 *  After: adds a single jsonobj to the succ List
	 */
	public void addSingleSucc(JSONObject newobj) {
		this.response.add(newobj);
	}

	/*-----------------------------------------------------------------------------*/
	/*---------------------------SUCCESS SETTER END--------------------------------*/
	/*-----------------------------------------------------------------------------*/

	// checks if there no errors present
	public boolean errorsExist() {
		if (this.error.size() > 0 || this.many_in_one_erros.length() > 0) {
			return true;
		}
		return false;
	}

	/* when we are about to return the response we add the wrappers */
	public String getErrorResponse() {
		if (this.many_in_one_erros.length() > 0) {
			this.error.add(this.many_in_one_erros);
		}
		return this.errors_response.put("BadResp", this.error).toString();
	}

	public String getSuccessResponse() {
		if (this.many_in_one_succ.length() > 0) {
			this.response.add(this.many_in_one_succ);
		}
		return this.errors_response.put("GoodResp", this.response).toString();
	}

}
