package com.civicresolve.controller;
import com.civicresolve.model.*; import com.civicresolve.repository.*; import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/admin") public class AdminController{
 private final ComplaintRepository repo;private final ComplaintHistoryRepository history;
 public AdminController(ComplaintRepository r,ComplaintHistoryRepository h){repo=r;history=h;}
 @GetMapping("/complaints") public List<Complaint> all(){return repo.findAll();}
 @PatchMapping("/complaints/{id}/status") public Complaint status(@PathVariable Long id,@RequestBody Map<String,String> b){
  var c=repo.findById(id).orElseThrow();String old=c.status;c.status=b.get("status");c.updatedAt=LocalDateTime.now();
  if("RESOLVED".equals(c.status))c.resolvedAt=LocalDateTime.now();var s=repo.save(c);
  var h=new ComplaintHistory();h.complaint=s;h.oldStatus=old;h.newStatus=s.status;h.comment=b.getOrDefault("comment","Status updated");history.save(h);return s;
 }
 @GetMapping("/analytics") public Map<String,Object> analytics(){
  return Map.of("total",repo.count(),"submitted",repo.countByStatus("SUBMITTED"),"underReview",repo.countByStatus("UNDER_REVIEW"),
   "inProgress",repo.countByStatus("IN_PROGRESS"),"resolved",repo.countByStatus("RESOLVED"));
 }
}