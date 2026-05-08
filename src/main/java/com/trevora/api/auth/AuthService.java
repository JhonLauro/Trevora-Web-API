package com.trevora.api.auth;

import com.trevora.api.auth.dto.AuthResponse;
import com.trevora.api.auth.dto.LoginRequest;
import com.trevora.api.auth.dto.RegisterRequest;
import com.trevora.api.auth.dto.UserDto;
import com.trevora.api.common.ConflictException;
import com.trevora.api.common.UnauthorizedException;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already exists.");
        }

        AppUser user = new AppUser();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPhoneNumber(request.phoneNumber().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole("TRAVELER");

        AppUser saved = users.save(user);
        return new AuthResponse(issueToken(), toDto(saved));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        AppUser user = users.findByEmailIgnoreCase(request.identifier().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        return new AuthResponse(issueToken(), toDto(user));
    }

    private String issueToken() {
        return "trevora-" + UUID.randomUUID();
    }

    private UserDto toDto(AppUser user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }
}
