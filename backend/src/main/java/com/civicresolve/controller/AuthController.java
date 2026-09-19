package com.civicresolve.controller;

import com.civicresolve.model.User;
import com.civicresolve.repository.UserRepository;
import com.civicresolve.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController @RequestMapping("/api/auth") public class AuthController{
 private final UserRepository users; private final PasswordEncoder enc; private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;enc=e;jwt=j;}
 @PostMapping("/register") public Map<String,Object> register(@RequestBody Map<String,String> input){
  String name=input.get("fullName"),email=input.get("email"),password=input.get("password");
  if(name==null||name.isBlank()||name.trim().length()>150||email==null||email.isBlank()||password==null||password.length()<8)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name, valid email and password (minimum 8 characters) are required");
  email=email.trim().toLowerCase(Locale.ROOT);if(users.findByEmail(email).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
  User u=new User();u.fullName=name.trim();u.email=email;u.passwordHash=enc.encode(password);u.role="CITIZEN";u.active=true;users.save(u);return Map.of("message","Registration successful");
 }
 @PostMapping("/login") public Map<String,Object> login(@RequestBody Map<String,String> input){
  String email=input.get("email"),password=input.get("password");var u=users.findByEmail(email==null?"":email.trim().toLowerCase(Locale.ROOT)).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));
  if(!u.active||!enc.matches(password==null?"":password,u.passwordHash))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
  return Map.of("token",jwt.create(u.email,u.role),"role",u.role,"name",u.fullName,"userId",u.id,"email",u.email);
 }
 @GetMapping("/me") public Map<String,Object> me(Authentication a){var u=users.findByEmail(a.getName()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"User not found"));return Map.of("userId",u.id,"name",u.fullName,"email",u.email,"role",u.role);}
}
