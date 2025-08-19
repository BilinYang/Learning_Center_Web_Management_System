package com.bilin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {

    @Test
    public void testGeneratorJwt(){
        // self-define payload (usually we store things like username and password and id)
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "Tom");

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "123456")
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 12*3600*1000)) // expires in 12 hours (this is in milliseconds)
                .compact();

        System.out.println(jwt);

    }

    @Test
    public void testParseJwt(){
        Claims claims = Jwts.parser().setSigningKey("123456")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJUb20iLCJleHAiOjQzMjAwfQ.LY4StyHt5sJS-IUX8w-uF-R8Qj5JOQdKprVqAGfWkCs")
                .getBody();
        System.out.println(claims);
    }
}
