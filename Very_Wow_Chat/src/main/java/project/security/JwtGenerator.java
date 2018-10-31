package project.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import project.persistance.entities.JwtUser;

import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    /* This will return the json webtoken*/
    public String generate(JwtUser jwtUser) {


        Claims claims = Jwts.claims()
                .setSubject(jwtUser.getUserName());
        claims.put("userId", String.valueOf(jwtUser.getId()));
        claims.put("role", jwtUser.getRole());


        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "mydicktasteslikelemons")
                .compact();
    }
}

