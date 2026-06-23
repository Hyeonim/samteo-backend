package com.samteo.domain.planner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.planner.dto.request.TransitRouteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class KakaoMobilityDirectionsService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${kakao.client.id:}")
    private String restApiKey;

    public KakaoMobilityDirectionsService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public JsonNode searchDrivingRoute(TransitRouteRequest request) {
        if (!StringUtils.hasText(restApiKey)) {
            throw new IllegalArgumentException("Kakao REST API key is not configured.");
        }
        if (request.getStartLatitude() == null || request.getStartLongitude() == null
                || request.getEndLatitude() == null || request.getEndLongitude() == null) {
            throw new IllegalArgumentException("Route coordinates are required.");
        }

        URI uri = UriComponentsBuilder
                .fromUriString("https://apis-navi.kakaomobility.com/v1/directions")
                .queryParam("origin", request.getStartLongitude() + "," + request.getStartLatitude())
                .queryParam("destination", request.getEndLongitude() + "," + request.getEndLatitude())
                .queryParam("priority", "RECOMMEND")
                .build()
                .encode()
                .toUri();

        try {
            String body = restClient.get()
                    .uri(uri)
                    .header("Authorization", "KakaoAK " + restApiKey)
                    .retrieve()
                    .body(String.class);
            return objectMapper.readTree(body);
        } catch (RestClientException e) {
            log.error("Kakao Mobility directions API call failed: {}", e.getMessage());
            throw new RuntimeException("Kakao Mobility directions API call failed.");
        } catch (Exception e) {
            log.error("Kakao Mobility directions response parsing failed: {}", e.getMessage());
            throw new RuntimeException("Kakao Mobility directions response parsing failed.");
        }
    }
}
