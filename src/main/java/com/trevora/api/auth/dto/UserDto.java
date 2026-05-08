package com.trevora.api.auth.dto;

public record UserDto(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        String role
) {
}
