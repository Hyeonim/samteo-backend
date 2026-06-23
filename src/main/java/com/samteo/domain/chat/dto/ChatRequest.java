package com.samteo.domain.chat.dto;

import java.util.List;

public record ChatRequest(String message, List<ChatMessageDto> history, String currentPath) {
}
