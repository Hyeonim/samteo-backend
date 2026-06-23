package com.samteo.domain.chat.controller;

import com.samteo.domain.chat.dto.ChatRequest;
import com.samteo.domain.chat.dto.ChatResponse;
import com.samteo.domain.chat.service.ChatService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(
            @AuthenticationPrincipal Long userId,
            @RequestBody ChatRequest request) {
        return ResponseEntity.ok(ApiResponse.success(chatService.reply(userId, request)));
    }
}
