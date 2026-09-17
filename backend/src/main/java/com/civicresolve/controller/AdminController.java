package com.civicresolve.controller;
import com.civicresolve.model.*; import com.civicresolve.repository.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import org.springframework.http.HttpStatus; import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/admin") public class AdminController{
 private final ComplaintRepository repo;private final ComplaintHistoryRepository history; private final UserRepository users;
 public AdminController(ComplaintRepository r,ComplaintHistoryRepository h,UserRepository u){repo=r;history=h;users=u;}
 @GetMapping("/complaints") public List<Complaint> all(Authentication authentication){currentAuthority(authentication);return repo.findAll();}
 @PatchMapping("/complaints/{id}/status") public Complaint status(@PathVariable Long id,@RequestBody Map<String,String> b, Authentication authentication){
  var c=repo.findById(id).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Complaint not found"));String old=c.status;String next=b.get("status");if(!List.of("SUBMITTED","UNDER_REVIEW","IN_PROGRESS","RESOLVED").contains(next))throw new IllegalArgumentException("Invalid complaint status");c.status=next;c.updatedAt=LocalDateTime.now();
  if("RESOLVED".equals(c.status))c.resolvedAt=LocalDateTime.now(); else c.resolvedAt=null;var s=repo.save(c);
  var h=new ComplaintHistory();h.complaint=s;h.changedBy=currentAuthority(authentication);h.oldStatus=old;h.newStatus=s.status;h.comment=b.getOrDefault("comment","Status updated");history.save(h);return s;
 }
 @GetMapping("/analytics") public Map<String,Object> analytics(Authentication authentication){
  currentAuthority(authentication);
  return Map.of("total",repo.count(),"submitted",repo.countByStatus("SUBMITTED"),"underReview",repo.countByStatus("UNDER_REVIEW"),
   "inProgress",repo.countByStatus("IN_PROGRESS"),"resolved",repo.countByStatus("RESOLVED"),
   "low",repo.countByPriority("LOW"),"medium",repo.countByPriority("MEDIUM"),"high",repo.countByPriority("HIGH"),"urgent",repo.countByPriority("URGENT"));
 }
 private User currentAuthority(Authentication authentication) {
  if (authentication == null || authentication.getName() == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required");
  User user = users.findByEmailIgnoreCase(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user no longer exists"));
  if (!user.active || !("ADMIN".equals(user.role) || "AUTHORITY".equals(user.role))) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authority access required");
  return user;
 }
}
