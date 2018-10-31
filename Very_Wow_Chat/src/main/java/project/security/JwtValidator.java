package project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import project.persistance.entities.JwtUser;

import org.springframework.stereotype.Component;

/* this class has to validate user jtw tokens that is has been passed */
@Component
public class JwtValidator {
	
  private String secret = "mydicktasteslikelemons";
  
  public JwtUser validate(String token) {
     JwtUser jwtUser = null;
     try { // get the tokens information 
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
             // create a token user for further validation
            jwtUser = new JwtUser();
            // the data to the user
            jwtUser.setUserName(body.getSubject());
            jwtUser.setId(Long.parseLong((String) body.get("userId")));
            jwtUser.setRole((String) body.get("role")); // important !!!
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return jwtUser;
    }
}
