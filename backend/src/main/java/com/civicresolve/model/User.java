package com.civicresolve.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity @Table(name="users") public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="full_name") public String fullName;
 @Column(unique=true,nullable=false) public String email;
 @JsonIgnore @Column(name="password_hash") public String passwordHash;
 public String phone; public String role="CITIZEN";
 @Column(name="is_active") public boolean active=true;
}
