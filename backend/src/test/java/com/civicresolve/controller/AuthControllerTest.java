package com.civicresolve.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.civicresolve.dto.LoginRequest;
import com.civicresolve.dto.RegistrationRequest;
import com.civicresolve.model.User;
import com.civicresolve.repository.UserRepository;
import com.civicresolve.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
 @Mock UserRepository users;

 @Test void registrationHashesPasswordAndNormalizesEmail() {
  AuthController controller = new AuthController(users, new BCryptPasswordEncoder(), new JwtService("test-secret-that-is-at-least-thirty-two-bytes"));
  when(users.findByEmailIgnoreCase("citizen@example.com")).thenReturn(Optional.empty());
  controller.register(new RegistrationRequest(" Citizen Name ", " Citizen@Example.com ", "safe-password"));
  ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
  verify(users).save(saved.capture());
  assertThat(saved.getValue().email).isEqualTo("citizen@example.com");
  assertThat(saved.getValue().role).isEqualTo("CITIZEN");
  assertThat(saved.getValue().passwordHash).isNotEqualTo("safe-password");
  assertThat(new BCryptPasswordEncoder().matches("safe-password", saved.getValue().passwordHash)).isTrue();
 }

 @Test void loginAcceptsMatchingHashAndCreatesParseableJwt() {
  JwtService jwt = new JwtService("test-secret-that-is-at-least-thirty-two-bytes");
  User user = new User(); user.id = 9L; user.email = "citizen@example.com"; user.fullName = "Citizen"; user.role = "CITIZEN"; user.passwordHash = new BCryptPasswordEncoder().encode("safe-password");
  when(users.findByEmailIgnoreCase("citizen@example.com")).thenReturn(Optional.of(user));
  var response = new AuthController(users, new BCryptPasswordEncoder(), jwt).login(new LoginRequest("CITIZEN@example.com", "safe-password"));
  assertThat(response.userId()).isEqualTo(9L);
  assertThat(jwt.parse(response.token()).getSubject()).isEqualTo("citizen@example.com");
  assertThat(jwt.parse(response.token()).get("role", String.class)).isEqualTo("CITIZEN");
 }

 @Test void loginRejectsInvalidPassword() {
  User user = new User(); user.email = "citizen@example.com"; user.passwordHash = new BCryptPasswordEncoder().encode("safe-password");
  when(users.findByEmailIgnoreCase("citizen@example.com")).thenReturn(Optional.of(user));
  assertThatThrownBy(() -> new AuthController(users, new BCryptPasswordEncoder(), new JwtService("test-secret-that-is-at-least-thirty-two-bytes")).login(new LoginRequest("citizen@example.com", "wrong-password")))
   .isInstanceOf(ResponseStatusException.class).extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
 }
}
