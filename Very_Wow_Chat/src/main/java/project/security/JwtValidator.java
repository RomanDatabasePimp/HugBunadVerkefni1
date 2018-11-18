package project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import project.payloads.JwtUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/* this class has to validate user jtw tokens that is has been passed */
@Component
public class JwtValidator {
  
  @Value("${cryptography.security.password}")
  private String secretKey;
  
  public JwtUser validate(String token) {
      
      String base64EncodedSecretKey = TextCodec.BASE64.encode(secretKey);
	  
     JwtUser jwtUser = null;
     try { // get the tokens information 
            Claims body = Jwts.parser()
                    .setSigningKey(base64EncodedSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
             // create a token user for further validation
            jwtUser = new JwtUser();
            // the data to the user
            jwtUser.setUserName(body.getSubject());
            jwtUser.setRole((String) body.get("role")); // important !!!
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return jwtUser;
    }
}
