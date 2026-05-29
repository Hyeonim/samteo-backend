package com.samteo.dto.response;

public record AuthResponse(String token, Long userId, String email, String name) {}
