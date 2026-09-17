package com.civicresolve.controller;

import com.civicresolve.dto.AuthResponse;
import com.civicresolve.dto.LoginRequest;
import com.civicresolve.dto.RegistrationRequest;
import com.civicresolve.dto.UserResponse;
import com.civicresolve.model.User;
import com.civicresolve.repository.UserRepository;
import com.civicresolve.security.JwtService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) { this.users = users; this.encoder = encoder; this.jwt = jwt; }

 @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
 public Map<String, String> register(@Valid @RequestBody RegistrationRequest request) {
  String email = normalizedEmail(request.email());
  if (users.findByEmailIgnoreCase(email).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
  User user = new User(); user.fullName = request.fullName().trim(); user.email = email;
  user.passwordHash = encoder.encode(request.password()); user.role = "CITIZEN"; user.active = true;
  users.save(user);
  return Map.of("message", "Registration successful");
 }

 @PostMapping("/login")
 public AuthResponse login(@Valid @RequestBody LoginRequest request) {
  User user = users.findByEmailIgnoreCase(normalizedEmail(request.email())).orElseThrow(this::invalidCredentials);
  if (!user.active || user.passwordHash == null || !encoder.matches(request.password(), user.passwordHash)) throw invalidCredentials();
  return new AuthResponse(jwt.create(user.email, user.role), user.role, user.fullName, user.id);
 }

 @GetMapping("/me")
 public UserResponse me(Authentication authentication) {
  if (authentication == null || authentication.getName() == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required");
  User user = users.findByEmailIgnoreCase(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user no longer exists"));
  if (!user.active) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is inactive");
  return new UserResponse(user.id, user.fullName, user.email, user.role, user.phone, user.language, user.notificationsEnabled);
 }

 private ResponseStatusException invalidCredentials() { return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"); }
 private String normalizedEmail(String email) { return email.trim().toLowerCase(Locale.ROOT); }
}
