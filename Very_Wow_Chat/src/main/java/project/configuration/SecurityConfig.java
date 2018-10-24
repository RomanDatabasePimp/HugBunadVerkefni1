package project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/* This class contains all the security implementation for our project 
 * here we can configure our security settings */
@Configuration
@EnableWebSecurity // Enable security config. This annotation denotes config for spring security.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	/* Atm there is only one user and its hugbo he has a role of admin we define what privlages
	 * admin gets its not prebuild, we can also create more users later on
	 * Authentication : Users -> roles */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
		.withUser("hugbo").password("verystrongpassword").roles("ADMIN");
		
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests().antMatchers("/**")
		    .hasRole("ADMIN").and().csrf().disable().headers().frameOptions().disable();

	}

}