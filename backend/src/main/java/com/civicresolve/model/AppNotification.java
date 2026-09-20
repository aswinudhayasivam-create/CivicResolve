package com.civicresolve.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="notifications") public class AppNotification {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public User user;
 @ManyToOne public Complaint complaint;
 @Column(length=500, nullable=false) public String message;
 @Column(name="is_read", nullable=false) public boolean read=false;
 public LocalDateTime createdAt=LocalDateTime.now();
}
