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

/**
 * This class is responsible for sending POST request to the email server that's
 * hosted on our Heroku server, https://hugbomailserver.herokuapp.com/. It will
 * send the email to the user with the specified content.
 * 
 * NOTE: Heroku web apps, i.e our webserver, goes to sleep ater 30 min of no use
 * (since its free version) so you might want to just open the link above in
 * your browser just to tell Heroku to wake up this app or else you might get
 * sometimes weird cases where the request was sent but nothing happen thats
 * because the server was still sleeping and heroku allowed the request go
 * through but the request was missed because the server as waking up
 * 
 * TODO: replace with Mailer.java
 */
public class MailController {

	private final String email; // Recipients email
	private final String content; // content of the email

	/**
	 * 
	 * @param email
	 * @param key
	 */
	public MailController(String email, String key) {
		this.email = email;
		this.content = "Welcome to VeryWowChat!!! \nbefore you can login please validate your account here : "
				+ "https://verywowchat.herokuapp.com/validation/" + key;
	}

	/**
	 * Try to send the email to the email server.
	 */
	public void send() {
		try {
			this.tryToSend();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send email to server (might crash)
	 * 
	 * @throws Exception
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
