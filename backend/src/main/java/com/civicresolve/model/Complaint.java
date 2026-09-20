package com.civicresolve.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="complaints") public class Complaint {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(unique=true) public String trackingNumber;
 @ManyToOne(optional=false) public User citizen;
 @ManyToOne(optional=false) public Category category;
 @Column(length=160) public String title; @Column(columnDefinition="TEXT") public String description;
 @Column(length=20, nullable=false) public String priority="MEDIUM"; @Column(length=30, nullable=false) public String status="SUBMITTED"; @Column(length=250) public String location;
 public Double latitude; public Double longitude;
 public Double duplicateScore; public Long duplicateOf;
 @ManyToOne public User assignedTo;
 public LocalDateTime createdAt=LocalDateTime.now(),updatedAt=LocalDateTime.now(),resolvedAt;
}
