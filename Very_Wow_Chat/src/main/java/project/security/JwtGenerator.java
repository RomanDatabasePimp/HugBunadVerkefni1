package project.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import project.payloads.JwtUser;

import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    /* This will return the json webtoken*/
    public String generate(JwtUser jwtUser) {
        Claims claims = Jwts.claims()
                .setSubject(jwtUser.getUserName());
        claims.put("role", jwtUser.getRole());
        
        String secretKey = "mydicktasteslikelemons";
        
        String base64EncodedSecretKey = TextCodec.BASE64.encode(secretKey);
        
        
        
        // byte[] base64EncodedSecretKey = Base64.encodeBase64(secretKey.getBytes());
        
        
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, base64EncodedSecretKey)
                .compact();
    }
}

