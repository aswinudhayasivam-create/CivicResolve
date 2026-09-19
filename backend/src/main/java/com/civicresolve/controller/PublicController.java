package com.civicresolve.controller;
import com.civicresolve.repository.ComplaintRepository;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/public") public class PublicController{
 private final ComplaintRepository repo; public PublicController(ComplaintRepository r){repo=r;}
 @GetMapping("/stats") public Map<String,Long> stats(){
  long total=repo.count(), resolved=repo.countByStatus("RESOLVED");
  long inProgress=repo.countByStatus("UNDER_REVIEW")+repo.countByStatus("IN_PROGRESS");
  long priority=repo.countByPriority("HIGH")+repo.countByPriority("URGENT");
  return Map.of("total",total,"resolved",resolved,"inProgress",inProgress,"priority",priority);
 }
}
