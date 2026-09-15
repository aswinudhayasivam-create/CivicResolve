package com.civicresolve.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="complaint_history") public class ComplaintHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public Complaint complaint; @ManyToOne public User changedBy;
 public String oldStatus; public String newStatus; @Column(columnDefinition="TEXT") public String comment;
 public LocalDateTime createdAt=LocalDateTime.now();
}