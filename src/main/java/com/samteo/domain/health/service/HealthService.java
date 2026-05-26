package com.samteo.domain.health.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public String getStatus() {
        return "Samteo server is running.";
    }
}
