package com.trevora.api.auth.dto;

public record AuthResponse(
        String token,
        UserDto user
) {
}
