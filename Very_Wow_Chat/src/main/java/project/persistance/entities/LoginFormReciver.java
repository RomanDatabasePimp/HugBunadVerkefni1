package project.persistance.entities;

/* Since java is a little bitch we need to make a class
 * that will serve as out json obj when reciving Json data from the client */
public class LoginFormReciver {
	// The username and password recived
	private final String username;
	private final String password;

	/* IMPLEMENT XSS */
	public LoginFormReciver(String us, String pass) {
		this.username = us;
		this.password = pass;
	}

	/*
	 * ------------------------ AUTO GEN GETTERS AND SETTERS --------------------------
	 */
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
