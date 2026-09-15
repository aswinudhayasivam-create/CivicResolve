package com.civicresolve.controller;
import com.civicresolve.model.*; import com.civicresolve.repository.*; import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/complaints") public class ComplaintController{
 private final ComplaintRepository repo;private final UserRepository users;private final CategoryRepository cats;private final ComplaintHistoryRepository history;
 public ComplaintController(ComplaintRepository r,UserRepository u,CategoryRepository c,ComplaintHistoryRepository h){repo=r;users=u;cats=c;history=h;}
 @GetMapping("/mine/{userId}") public List<Complaint> mine(@PathVariable Long userId){return repo.findByCitizenIdOrderByCreatedAtDesc(userId);}
 @GetMapping("/track/{tracking}") public Map<String,Object> track(@PathVariable String tracking){
  var c=repo.findByTrackingNumber(tracking).orElseThrow(); return Map.of("complaint",c,"history",history.findByComplaintIdOrderByCreatedAtAsc(c.id));
 }
 @PostMapping public Complaint create(@RequestBody Complaint c){
  if(c.title==null||c.title.isBlank()||c.description==null||c.description.isBlank())throw new IllegalArgumentException("Title and description are required");
  c.citizen=users.findById(c.citizen.id).orElseThrow();c.category=cats.findById(c.category.id).orElseThrow();
  c.trackingNumber="GRV-"+System.currentTimeMillis();c.status="SUBMITTED";c.createdAt=LocalDateTime.now();c.updatedAt=c.createdAt;
  var saved=repo.save(c);var h=new ComplaintHistory();h.complaint=saved;h.newStatus="SUBMITTED";h.comment="Complaint submitted";history.save(h);return saved;
 }
}