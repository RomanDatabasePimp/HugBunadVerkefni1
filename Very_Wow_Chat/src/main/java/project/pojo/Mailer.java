package project.pojo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class to send email.
 * 
 * "Generalization" of MessageController.java.
 */
public class Mailer {

	private final String recipientEmail;
	private final String emailContent;
	private final String serverUrl;
	private final String secretKey;

	/**
	 * Create a email to send.
	 * 
	 * @param recipientEmail who to send email to
	 * @param emailContent   contents of email
	 * @param serverUrl      supplied by Spring
	 * @param secretKey      supplied by Spring
	 */
	public Mailer(String recipientEmail, String emailContent, String serverUrl, String secretKey) {
		this.recipientEmail = recipientEmail;
		this.emailContent = emailContent;
		this.serverUrl = serverUrl;
		this.secretKey = secretKey;
	}

	/**
	 * Sends a message, and if it fails then it'll only print a stack trace.
	 */
	public void send() {
		try {
			this.tryToSend();
		} catch (Exception e) {
			System.out.println("send error");
			e.printStackTrace();
		}
	}

	/**
	 * Try to send message. If it fails then it raises an exception.
	 * 
	 * @throws Exception
	 */
	public void tryToSend() throws Exception {
		System.out.println("try to send");
		URL url = new URL(serverUrl);

		LinkedHashMap<String, String> params = new LinkedHashMap<>();
		params.put("toMail", this.recipientEmail);
		params.put("content", this.emailContent);
		params.put("seckey", secretKey);

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

		System.out.println("try to send end");
	}

}
