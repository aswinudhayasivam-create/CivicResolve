package com.civicresolve.controller;

import com.civicresolve.model.*;
import com.civicresolve.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {
    private static final String TRACKING_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final ComplaintRepository repo;
    private final ComplaintHistoryRepository history;
    private final UserRepository users;
    private final CategoryRepository categories;

    public ComplaintController(ComplaintRepository repo, ComplaintHistoryRepository history, UserRepository users, CategoryRepository categories) {
        this.repo = repo; this.history = history; this.users = users; this.categories = categories;
    }

    private User citizen(Authentication a) {
        return users.findByEmail(a.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Map<String, Object> safe(Complaint c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", c.id); m.put("trackingNumber", c.trackingNumber); m.put("title", c.title);
        m.put("description", c.description); m.put("priority", c.priority); m.put("status", c.status);
        m.put("location", c.location == null ? "" : c.location); m.put("createdAt", c.createdAt); m.put("updatedAt", c.updatedAt); m.put("resolvedAt", c.resolvedAt);
        Map<String,Object> cat = new LinkedHashMap<>();
        cat.put("id", c.category == null ? null : c.category.id); cat.put("name", c.category == null ? "Unknown" : c.category.name); m.put("category", cat);
        if (c.duplicateScore != null) m.put("duplicateScore", c.duplicateScore);
        return m;
    }

    @GetMapping("/mine")
    public List<Map<String,Object>> mine(Authentication a) {
        return repo.findByCitizenIdOrderByCreatedAtDesc(citizen(a).id).stream().map(this::safe).toList();
    }

    @GetMapping("/track/{tracking}")
    public Map<String,Object> track(@PathVariable String tracking) {
        String code = tracking == null ? "" : tracking.trim();
        var c = repo.findByTrackingNumber(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        var safeHistory = history.findByComplaintIdOrderByCreatedAtAsc(c.id).stream().map(h -> {
            Map<String,Object> x = new LinkedHashMap<>(); x.put("newStatus", h.newStatus); x.put("comment", h.comment == null ? "" : h.comment); x.put("createdAt", h.createdAt); return x;
        }).toList();
        return Map.of("complaint", safe(c), "history", safeHistory);
    }

    private String trackingCode() {
        StringBuilder b = new StringBuilder("GRV-");
        for (int i = 0; i < 12; i++) b.append(TRACKING_ALPHABET.charAt(RANDOM.nextInt(TRACKING_ALPHABET.length())));
        return b.toString();
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> create(@RequestBody Map<String,Object> input, Authentication a) {
        String title = Objects.toString(input.get("title"), "").trim();
        String description = Objects.toString(input.get("description"), "").trim();
        String priority = Objects.toString(input.get("priority"), "MEDIUM").trim().toUpperCase(Locale.ROOT);
        String location = Objects.toString(input.get("location"), "").trim();
        if (title.isBlank() || title.length() > 160) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required and must be 160 characters or fewer");
        if (description.length() < 10 || description.length() > 5000) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description must be between 10 and 5000 characters");
        if (!List.of("LOW","MEDIUM","HIGH","URGENT").contains(priority)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid priority");
        if (location.length() > 250) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is too long");
        Long categoryId = null;
        Object category = input.get("category");
        if (category instanceof Map<?,?> map && map.get("id") != null) categoryId = Long.valueOf(String.valueOf(map.get("id")));
        if (categoryId == null && input.get("categoryId") != null) {
            try { categoryId = Long.valueOf(String.valueOf(input.get("categoryId"))); }
            catch (NumberFormatException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category"); }
        }
        if (categoryId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category is required");
        var cat = categories.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
        var c = new Complaint(); c.title = title; c.description = description; c.priority = priority; c.location = location.isBlank()?null:location;
        c.citizen = citizen(a); c.category = cat; c.trackingNumber = trackingCode(); c.status = "SUBMITTED"; c.createdAt = LocalDateTime.now(); c.updatedAt = c.createdAt;
        var saved = repo.save(c);
        var h = new ComplaintHistory(); h.complaint = saved; h.newStatus = "SUBMITTED"; h.comment = "Complaint submitted"; history.save(h);
        return safe(saved);
    }
}
