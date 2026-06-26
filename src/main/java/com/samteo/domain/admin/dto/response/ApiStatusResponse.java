package com.samteo.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiStatusResponse {
    private String name;
    private String status;   // OK | ERROR | NO_KEY
    private long responseTimeMs;
    private String message;

    public static ApiStatusResponse ok(String name, long ms) {
        return new ApiStatusResponse(name, "OK", ms, null);
    }

    public static ApiStatusResponse error(String name, long ms, String message) {
        return new ApiStatusResponse(name, "ERROR", ms, message);
    }

    public static ApiStatusResponse noKey(String name) {
        return new ApiStatusResponse(name, "NO_KEY", 0, "API 키가 설정되지 않았습니다");
    }
}
