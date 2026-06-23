package com.samteo.domain.chat.dto;

import java.util.List;

public record ChatResponse(String message, String mode, List<String> suggestions) {
}
