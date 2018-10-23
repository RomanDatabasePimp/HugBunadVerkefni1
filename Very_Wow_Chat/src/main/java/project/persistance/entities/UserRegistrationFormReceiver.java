package project.persistance.entities;

public class UserRegistrationFormReceiver {

	private String userName;
	private String password;
	private String displayName;
	private String email;
	
	public UserRegistrationFormReceiver(String userName, String password, String displayName, String email) {
		this.userName = userName;
		this.password = password;
		this.displayName = displayName;
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getEmail() {
		return email;
	}
}
