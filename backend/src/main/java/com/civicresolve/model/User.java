package com.civicresolve.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
@Entity @Table(name="users") public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="full_name") public String fullName;
 @Column(unique=true,nullable=false) public String email;
 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @Column(name="password_hash") public String passwordHash;
 public String phone; public String role="CITIZEN";
 @Column(name="language", nullable=false) public String language="en";
 @Column(name="notifications_enabled", nullable=false) public boolean notificationsEnabled=true;
 @Column(name="is_active") public boolean active=true;
}
