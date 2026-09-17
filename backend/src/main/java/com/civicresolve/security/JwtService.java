package com.civicresolve.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets; import java.util.*; import javax.crypto.SecretKey;
@Service public class JwtService{
 private final SecretKey key;
 public JwtService(@Value("${app.jwt.secret}")String s){if(s==null||s.length()<32)throw new IllegalStateException("JWT_SECRET must be at least 32 characters");key=Keys.hmacShaKeyFor(s.getBytes(StandardCharsets.UTF_8));}
 public String create(String email,String role){return Jwts.builder().subject(email).claim("role",role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+86400000)).signWith(key).compact();}
 public Claims parse(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
}
