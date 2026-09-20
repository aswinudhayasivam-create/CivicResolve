package com.civicresolve.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="complaint_feedback") public class ComplaintFeedback {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne(optional=false) public Complaint complaint;
 @ManyToOne(optional=false) public User citizen;
 @Column(nullable=false) public int rating;
 @Column(length=1000) public String comment;
 public LocalDateTime createdAt=LocalDateTime.now();
}
