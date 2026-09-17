package com.civicresolve.controller;
import com.civicresolve.model.User; import com.civicresolve.repository.UserRepository; import com.civicresolve.security.JwtService; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/auth") public class AuthController{
 private final UserRepository users; private final PasswordEncoder enc; private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;enc=e;jwt=j;}
 @PostMapping("/register") public Map<String,Object> register(@RequestBody User u){
  if(u.email==null||u.email.isBlank()||u.passwordHash==null||u.passwordHash.length()<8||u.fullName==null||u.fullName.isBlank())throw new IllegalArgumentException("Name, email and an 8-character password are required");
  if(users.findByEmail(u.email).isPresent())throw new IllegalArgumentException("Email already registered");
  u.passwordHash=enc.encode(u.passwordHash);u.role="CITIZEN";users.save(u);return Map.of("message","Registration successful");
 }
 @PostMapping("/login") public Map<String,Object> login(@RequestBody User input){
  if(input.email==null||input.passwordHash==null)throw new IllegalArgumentException("Invalid credentials");var u=users.findByEmail(input.email).orElseThrow(()->new IllegalArgumentException("Invalid credentials"));
  if(!enc.matches(input.passwordHash,u.passwordHash))throw new IllegalArgumentException("Invalid credentials");
  return Map.of("token",jwt.create(u.email,u.role),"role",u.role,"name",u.fullName,"userId",u.id);
 }
}
