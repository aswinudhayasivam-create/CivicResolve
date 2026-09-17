package com.civicresolve.controller;

import com.civicresolve.dto.ComplaintRequest;
import com.civicresolve.model.Complaint;
import com.civicresolve.model.ComplaintHistory;
import com.civicresolve.repository.CategoryRepository;
import com.civicresolve.repository.ComplaintHistoryRepository;
import com.civicresolve.repository.ComplaintRepository;
import com.civicresolve.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {
    private final ComplaintRepository complaints;
    private final UserRepository users;
    private final CategoryRepository categories;
    private final ComplaintHistoryRepository history;
    public ComplaintController(ComplaintRepository complaints, UserRepository users, CategoryRepository categories, ComplaintHistoryRepository history) {
        this.complaints = complaints; this.users = users; this.categories = categories; this.history = history;
    }
    @GetMapping("/mine") public List<Complaint> mine(Authentication authentication) {
        return complaints.findByCitizenIdOrderByCreatedAtDesc(currentCitizen(authentication).id);
    }
    @GetMapping("/track/{tracking}") public Map<String, Object> track(@PathVariable String tracking) {
        var complaint = complaints.findByTrackingNumber(tracking).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        return Map.of("complaint", complaint, "history", history.findByComplaintIdOrderByCreatedAtAsc(complaint.id));
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Complaint create(@Valid @RequestBody ComplaintRequest request, Authentication authentication) {
        var citizen = currentCitizen(authentication);
        var category = categories.findById(request.categoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected category does not exist"));
        var now = LocalDateTime.now(); var complaint = new Complaint();
        complaint.title = request.title().trim(); complaint.description = request.description().trim();
        complaint.location = request.location() == null ? null : request.location().trim();
        complaint.priority = request.priority() == null ? "MEDIUM" : request.priority();
        complaint.citizen = citizen; complaint.category = category; complaint.trackingNumber = "GRV-" + System.currentTimeMillis();
        complaint.status = "SUBMITTED"; complaint.createdAt = now; complaint.updatedAt = now;
        var saved = complaints.save(complaint); var event = new ComplaintHistory();
        event.complaint = saved; event.newStatus = "SUBMITTED"; event.comment = "Complaint submitted"; history.save(event);
        return saved;
    }
    private com.civicresolve.model.User currentCitizen(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required");
        return users.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user no longer exists"));
    }
}
