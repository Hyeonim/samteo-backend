package com.samteo.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public String getStatus() {
        return "Samteo 서버가 정상 동작 중입니다.";
    }
}
