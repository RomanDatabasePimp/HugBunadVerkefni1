package project.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import project.persistance.entities.JwtAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

	public JwtAuthenticationTokenFilter() {
		super("/bigdickus");
	}

	/* THis is where the jtw tokens will be decoded and authenticated */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		String header = httpServletRequest.getHeader("Authorization");// in the header we get Authorisation
		
		// if Authorisation was not found or if the value of it starts with Token we
		// pass an error  the startsWith stuff is just something that i append to the start of the token
		if (header == null || !header.startsWith("Token ")) {
			throw new RuntimeException("JWT Token is missing");
		}
		
		// reason we start from 6 its cuz the we need to remember i appended Token in the start
		String authenticationToken = header.substring(6);

		JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);
		// let our authentication manager handle all the token shit
		return getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}
}
