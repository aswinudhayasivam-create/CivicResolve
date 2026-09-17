package com.civicresolve.controller;
import com.civicresolve.dto.*;
import com.civicresolve.model.User;
import com.civicresolve.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/account") public class AccountController {
 private final UserRepository users; private final PasswordEncoder encoder;
 public AccountController(UserRepository users, PasswordEncoder encoder) { this.users=users;this.encoder=encoder; }
 @GetMapping("/profile") public UserResponse profile(Authentication a) { return response(current(a)); }
 @PatchMapping("/profile") public UserResponse update(@Valid @RequestBody ProfileUpdateRequest r, Authentication a) { User u=current(a);u.fullName=r.name().trim();u.phone=blank(r.phone());u.language="ta".equals(r.language())?"ta":"en";u.notificationsEnabled=r.notificationsEnabled()==null||r.notificationsEnabled();return response(users.save(u)); }
 @PostMapping("/password") @ResponseStatus(HttpStatus.NO_CONTENT) public void password(@Valid @RequestBody PasswordChangeRequest r, Authentication a) { User u=current(a);if(!encoder.matches(r.currentPassword(),u.passwordHash))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Current password is incorrect");if(encoder.matches(r.newPassword(),u.passwordHash))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Choose a new password that differs from your current password");u.passwordHash=encoder.encode(r.newPassword());users.save(u); }
 private User current(Authentication a) { if(a==null||a.getName()==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Authentication is required");User u=users.findByEmailIgnoreCase(a.getName()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Authenticated user no longer exists"));if(!u.active)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Account is inactive");return u; }
 private String blank(String v) { return v==null||v.trim().isEmpty()?null:v.trim(); }
 private UserResponse response(User u) { return new UserResponse(u.id,u.fullName,u.email,u.role,u.phone,u.language==null?"en":u.language,u.notificationsEnabled); }
}
