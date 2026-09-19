package com.civicresolve.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(name = "full_name", nullable = false) public String fullName;
    @Column(unique = true, nullable = false) public String email;
    @Column(name = "password_hash", nullable = false) public String passwordHash;
    @Column(length = 30) public String phone;
    @Column(length = 20, nullable = false) public String role = "CITIZEN";
    @Column(name = "is_active", nullable = false) public boolean active = true;
    @Column(length = 10, nullable = false) public String language = "en";
    @Column(name = "notifications_enabled", nullable = false) public boolean notificationsEnabled = true;
}
