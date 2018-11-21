package project.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* This calss is responsible for sending POST request to the Email server thats hosted 
 * on our Heroku https://hugbomailserver.herokuapp.com/  it will send the email to the user
 * with the specified content.
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * heroku webapps i.e our webserver go to sleep in 30 min after no use (Since its free version)
 * so you might want to just open the link above in ur borwser just to tell heroku to wake up this app
 * or else you might get sometimes wierd cases where the reuqest was sent but nothing happen thats cuz
 * the server was still sleeping and i dont know how high heroku was not waking it up */
public class MailController {

	private final String email; // Recipients email
	private final String content;

	public MailController(String email, String key) {
		this.email = email;
		this.content = "Welcome to VeryWowChat!!! \nbefore you can login please validate your account here : "
				+ "https://verywowchat.herokuapp.com/validation/" + key;
	}

	/*
	 * Usage : send() For : ekkert After : tries to call the tryToSend method to
	 * send the email
	 */
	public void send() {
		try {
			this.tryToSend();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Usage : tryToSend() For : nothing After : create a json obj with parameters
	 * toMail,content,seckey that will be sent to the webserver
	 */
	public void tryToSend() throws Exception {
		URL url = new URL("https://hugbomailserver.herokuapp.com/"); // url to preform the http request on
		// data that will be sent to the email
		Map<String, String> params = new LinkedHashMap<>();
		params.put("toMail", this.email);
		params.put("content", this.content);
		params.put("seckey", "VeryStrongPassword");
		/*
		 * I wrote this last semester for hugbunadar verkefni 1 i am abit fuzzy on the
		 * details on how it worked a 100% :(
		 */
		StringBuilder postData = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> param = (Map.Entry<String, String>) it.next();
			if (postData.length() != 0) {
				postData.append('&');
			}
			postData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (int c; (c = in.read()) >= 0;) {
			sb.append((char) c);
		}
		String response = sb.toString();
		System.out.println(response);
	}

}
