package com.civicresolve.dto;

public record AuthResponse(String token, String role, String name, Long userId) {}
