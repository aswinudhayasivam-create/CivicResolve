package com.civicresolve.dto;

public record UserResponse(Long userId, String name, String email, String role, String phone, String language, boolean notificationsEnabled) {}
