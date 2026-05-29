package com.samteo.global.response;

public record AuthResponse(String token, Long userId, String email, String name) {}
