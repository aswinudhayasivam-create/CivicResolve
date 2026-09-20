package com.civicresolve.controller;

import com.civicresolve.model.*;
import com.civicresolve.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final ComplaintRepository repo; private final ComplaintHistoryRepository history; private final UserRepository users;
    public AdminController(ComplaintRepository r, ComplaintHistoryRepository h, UserRepository u){repo=r;history=h;users=u;}
    private User authority(Authentication a){ return users.findByEmail(a.getName()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Authority account not found")); }
    private Map<String,Object> safe(Complaint c){
        Map<String,Object> m=new LinkedHashMap<>(); m.put("id",c.id);m.put("trackingNumber",c.trackingNumber);m.put("title",c.title);m.put("description",c.description);m.put("priority",c.priority);m.put("status",c.status);m.put("location",c.location==null?"":c.location);m.put("latitude",c.latitude);m.put("longitude",c.longitude);m.put("createdAt",c.createdAt);m.put("updatedAt",c.updatedAt);m.put("resolvedAt",c.resolvedAt);m.put("duplicateScore",c.duplicateScore);m.put("duplicateOf",c.duplicateOf);
        Map<String,Object> cat=new LinkedHashMap<>();cat.put("id",c.category==null?null:c.category.id);cat.put("name",c.category==null?"Unknown":c.category.name);m.put("category",cat);
        if(c.citizen!=null)m.put("citizen",Map.of("fullName",c.citizen.fullName,"email",c.citizen.email,"phone",c.citizen.phone==null?"":c.citizen.phone));
        if(c.assignedTo!=null)m.put("assignedTo",Map.of("fullName",c.assignedTo.fullName,"email",c.assignedTo.email));
        return m;
    }
    @GetMapping("/complaints") public List<Map<String,Object>> all(Authentication a){authority(a);return repo.findAll().stream().sorted(Comparator.comparing((Complaint c)->c.createdAt,Comparator.nullsLast(Comparator.reverseOrder()))).map(this::safe).toList();}
    @GetMapping("/complaints/{id}/history") public List<Map<String,Object>> complaintHistory(@PathVariable Long id, Authentication a){ authority(a);
        if(!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Complaint not found");
        return history.findByComplaintIdOrderByCreatedAtAsc(id).stream().map(h->{Map<String,Object> m=new LinkedHashMap<>();m.put("oldStatus",h.oldStatus);m.put("newStatus",h.newStatus);m.put("comment",h.comment==null?"":h.comment);m.put("createdAt",h.createdAt);return m;}).toList();
    }
    @PatchMapping("/complaints/{id}/status") @Transactional public Map<String,Object> status(@PathVariable Long id,@RequestBody Map<String,String> b, Authentication a){
        User actor = authority(a);
        var c=repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Complaint not found"));String next=Objects.toString(b.get("status"),"").toUpperCase(Locale.ROOT);
        if(!List.of("SUBMITTED","UNDER_REVIEW","IN_PROGRESS","RESOLVED").contains(next))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid complaint status");
        String old=c.status;c.status=next;c.updatedAt=LocalDateTime.now();c.resolvedAt="RESOLVED".equals(next)?c.updatedAt:null;var saved=repo.save(c);
        var h=new ComplaintHistory();h.complaint=saved;h.changedBy=actor;h.oldStatus=old;h.newStatus=saved.status;h.comment=Objects.toString(b.get("comment"),"Status updated by authority").trim();
        if(h.comment.length()>1000) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Comment must be 1000 characters or fewer");if(h.comment.isBlank())h.comment="Status updated by authority";history.save(h);return safe(saved);
    }
    @PatchMapping("/complaints/{id}/assign-self")
    public Map<String,Object> assignSelf(@PathVariable Long id, Authentication a) {
        User actor = authority(a);
        Complaint c = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        c.assignedTo = actor;
        c.updatedAt = LocalDateTime.now();
        Complaint saved = repo.save(c);
        var h = new ComplaintHistory(); h.complaint = saved; h.changedBy = actor; h.oldStatus = saved.status; h.newStatus = "ASSIGNED"; h.comment = "Complaint assigned to an authority"; history.save(h);
        return safe(saved);
    }

    @GetMapping("/analytics") public Map<String,Object> analytics(Authentication a){ authority(a);return Map.of("total",repo.count(),"submitted",repo.countByStatus("SUBMITTED"),"underReview",repo.countByStatus("UNDER_REVIEW"),"inProgress",repo.countByStatus("IN_PROGRESS"),"resolved",repo.countByStatus("RESOLVED"),"highPriority",repo.countByPriority("HIGH"),"urgent",repo.countByPriority("URGENT"));}
}
