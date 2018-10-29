package project.persistance.entities;


/**
 * This class is for wrapping data in json objects
 * @author Vilhelml
 * @since 20.10.18
 *
 */
public class UserResponder {
	private String username;
	private String password;
	private String displayName;
	private String email;
	private Long created;
	
	public UserResponder(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.displayName = user.getDisplayName();
		this.email = user.getEmail();
		this.created = user.getCreated();
	}
	
	public UserResponder(String username, String password, String displayName, String email, Long created) {
		this.username = username;
		this.password = password;
		this.displayName = displayName;
		this.email = email;
		this.created = created;
	}
	
	// getters

	public String getUsername() {
		return username;
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

	public Long getCreated() {
		return created;
	}
	
	
}
