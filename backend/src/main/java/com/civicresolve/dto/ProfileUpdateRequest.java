package com.civicresolve.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record ProfileUpdateRequest(@NotBlank(message = "Full name is required") @Size(max = 150, message = "Full name is too long") String name, @Size(max = 30, message = "Phone number is too long") String phone, String language, Boolean notificationsEnabled) {}
