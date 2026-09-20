package com.civicresolve.controller;

import com.civicresolve.model.*; import com.civicresolve.repository.*;
import org.springframework.http.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; import java.util.*;

@RestController @RequestMapping("/api/notifications") public class NotificationController {
 private final NotificationRepository notifications; private final UserRepository users;
 public NotificationController(NotificationRepository n,UserRepository u){notifications=n;users=u;}
 private User current(Authentication a){return users.findByEmail(a.getName()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"User not found"));}
 private Map<String,Object> safe(AppNotification n){Map<String,Object> m=new LinkedHashMap<>();m.put("id",n.id);m.put("message",n.message);m.put("read",n.read);m.put("createdAt",n.createdAt);if(n.complaint!=null)m.put("trackingNumber",n.complaint.trackingNumber);return m;}
 @GetMapping public Map<String,Object> all(Authentication a){User u=current(a);return Map.of("unreadCount",notifications.countByUserIdAndReadFalse(u.id),"items",notifications.findByUserIdOrderByCreatedAtDesc(u.id).stream().limit(100).map(this::safe).toList());}
 @PatchMapping("/{id}/read") public Map<String,Object> read(@PathVariable Long id,Authentication a){User u=current(a);AppNotification n=notifications.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Notification not found"));if(!Objects.equals(n.user.id,u.id))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");n.read=true;notifications.save(n);return safe(n);}
 @PatchMapping("/read-all") @ResponseStatus(HttpStatus.NO_CONTENT) public void readAll(Authentication a){User u=current(a);var items=notifications.findByUserIdOrderByCreatedAtDesc(u.id);items.forEach(n->n.read=true);notifications.saveAll(items);}
}
