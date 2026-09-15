package com.civicresolve.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="complaints") public class Complaint {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(unique=true) public String trackingNumber;
 @ManyToOne(optional=false) public User citizen;
 @ManyToOne(optional=false) public Category category;
 public String title; @Column(columnDefinition="TEXT") public String description;
 public String priority="MEDIUM"; public String status="SUBMITTED"; public String location;
 public Double duplicateScore; public Long duplicateOf;
 @ManyToOne public User assignedTo;
 public LocalDateTime createdAt=LocalDateTime.now(),updatedAt=LocalDateTime.now(),resolvedAt;
}