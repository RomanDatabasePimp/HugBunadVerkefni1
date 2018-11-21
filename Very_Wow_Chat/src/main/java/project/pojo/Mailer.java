package project.pojo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Mailer {
	
	private final String recipientEmail;
	private final String emailContent;
	private final String serverUrl;
	private final String secretKey;	

	public Mailer(String recipientEmail, String emailContent, String serverUrl, String secretKey) {
		this.recipientEmail = recipientEmail;
		this.emailContent = emailContent;
		this.serverUrl = serverUrl;
		this.secretKey = secretKey;
	}

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
		URL url = new URL(serverUrl); // url to preform the http request on
		
		
		// data that will be sent to the email
		LinkedHashMap<String, String> params = new LinkedHashMap<>();
		params.put("toMail", this.recipientEmail);
		params.put("content", this.emailContent);
		params.put("seckey", secretKey);
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
