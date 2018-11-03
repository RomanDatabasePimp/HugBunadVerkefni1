package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import project.persistance.entities.JwtAuthenticationToken;
import project.persistance.entities.JwtUser;
import project.persistance.entities.JwtUserDetails;

import java.util.List;

/* THis is where the so called authentication happens */
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private JwtValidator validator;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

	}

	/* method that we call when a token arrives */
	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
		// convert it into our implementation of the token
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
		String token = jwtAuthenticationToken.getToken(); // get the token in string form

		JwtUser jwtUser = validator.validate(token);
		// if no token was recived then the user was not created we need to handle that
		if (jwtUser == null) {
			throw new RuntimeException("JWT Token is incorrect");
		}
		/*
		 * the jwt will have an role variable and its important atm we will only have
		 * one role but we can scale the app f.x add aminds and users and privilaged
		 * users and we denote their privilages by their roles so this offers full
		 * scalabilty
		 */
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(jwtUser.getRole());
		// return the details
		return new JwtUserDetails(jwtUser.getUserName(), token, grantedAuthorities);
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
	}
}
