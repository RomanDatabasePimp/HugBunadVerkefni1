package project.persistance.entities;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/* this is where we store our jwt token */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private String token;

	public JwtAuthenticationToken(String token) {
		super(null, null);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	// dont need these two functions but still have to overwite it
	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}
}
