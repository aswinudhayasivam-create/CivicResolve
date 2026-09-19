package com.civicresolve.controller;

import com.civicresolve.model.User;
import com.civicresolve.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AccountController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    private User current(Authentication authentication) {
        return users.findByEmail(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Map<String, Object> response(User u) {
        return Map.of(
            "userId", u.id,
            "fullName", u.fullName,
            "email", u.email,
            "role", u.role,
            "phone", u.phone == null ? "" : u.phone,
            "language", u.language == null ? "en" : u.language,
            "notificationsEnabled", u.notificationsEnabled
        );
    }

    @GetMapping("/profile")
    public Map<String, Object> profile(Authentication authentication) {
        return response(current(authentication));
    }

    @PatchMapping("/profile")
    public Map<String, Object> updateProfile(@RequestBody Map<String, Object> body, Authentication authentication) {
        User u = current(authentication);
        String name = body.get("name") == null ? u.fullName : String.valueOf(body.get("name")).trim();
        String phone = body.get("phone") == null ? "" : String.valueOf(body.get("phone")).trim();
        String language = body.get("language") == null ? "en" : String.valueOf(body.get("language")).trim().toLowerCase(Locale.ROOT);
        if (name.isBlank() || name.length() > 150) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is required and must be 150 characters or fewer");
        if (phone.length() > 30) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is too long");
        if (!language.equals("en") && !language.equals("ta")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported language");
        u.fullName = name;
        u.phone = phone.isBlank() ? null : phone;
        u.language = language;
        if (body.containsKey("notificationsEnabled")) u.notificationsEnabled = Boolean.parseBoolean(String.valueOf(body.get("notificationsEnabled")));
        users.save(u);
        return response(u);
    }

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody Map<String, String> body, Authentication authentication) {
        User u = current(authentication);
        String currentPassword = body.getOrDefault("currentPassword", "");
        String newPassword = body.getOrDefault("newPassword", "");
        if (!encoder.matches(currentPassword, u.passwordHash)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        if (newPassword.length() < 8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must contain at least 8 characters");
        u.passwordHash = encoder.encode(newPassword);
        users.save(u);
    }
}
