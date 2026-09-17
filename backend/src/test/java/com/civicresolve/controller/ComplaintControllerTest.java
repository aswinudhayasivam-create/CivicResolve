package com.civicresolve.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.civicresolve.dto.ComplaintRequest;
import com.civicresolve.model.User;
import com.civicresolve.repository.CategoryRepository;
import com.civicresolve.repository.ComplaintHistoryRepository;
import com.civicresolve.repository.ComplaintRepository;
import com.civicresolve.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ComplaintControllerTest {
 @Mock ComplaintRepository complaints;
 @Mock UserRepository users;
 @Mock CategoryRepository categories;
 @Mock ComplaintHistoryRepository history;

 @Test void complaintWithMissingCategoryReturnsAClientErrorInsteadOfNoSuchElement() {
  User citizen = new User(); citizen.email = "citizen@example.com"; citizen.active = true;
  when(users.findByEmailIgnoreCase("citizen@example.com")).thenReturn(Optional.of(citizen));
  when(categories.findById(999L)).thenReturn(Optional.empty());
  ComplaintController controller = new ComplaintController(complaints, users, categories, history);
  var authentication = new UsernamePasswordAuthenticationToken("citizen@example.com", null);
  assertThatThrownBy(() -> controller.create(new ComplaintRequest("Title", "Description", 999L, "MEDIUM", "Location"), authentication))
   .isInstanceOf(ResponseStatusException.class).extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
 }
}
