package project.payloads;

// Encrypt email
public class UserRegistrationFormReceiver {

	private final String userName;
	private final String displayName;
	private final String password;
	private final String passwordReap;
	private final String email;
	
	public UserRegistrationFormReceiver(String userName,String displayName, String password,String passwordReap ,String email) {
		this.userName = userName.toLowerCase();//want to have all lowercase
		this.displayName = displayName;
		this.password = password;
		this.passwordReap = passwordReap;
		this.email = email;
	}
	
	public boolean allInfoExists() {
		if(this.userName == null || this.displayName == null || this.password == null
		   || this.passwordReap == null || this.email == null) { return false; }
		return true;
	}

	public String getUserName() {
		return userName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordReap() {
		return passwordReap;
	}

	public String getEmail() {
		return email;
	}

}
