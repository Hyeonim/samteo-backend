package com.samteo.domain.planner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.planner.dto.request.CommuteRouteRequest;
import com.samteo.domain.planner.dto.request.TransitRouteRequest;
import com.samteo.domain.planner.dto.response.TransitAnchorResponse;
import com.samteo.domain.planner.dto.response.TransitRouteOptionResponse;
import com.samteo.domain.planner.dto.response.TransitRouteResponse;
import com.samteo.domain.planner.dto.response.TransitSubPathResponse;
import com.samteo.domain.planner.entity.Accommodation;
import com.samteo.domain.planner.entity.Job;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OdsayTransitService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;

    @Value("${external.odsay.base-url:https://api.odsay.com/v1/api}")
    private String baseUrl;

    @Value("${external.odsay.api-key:}")
    private String apiKey;

    public OdsayTransitService(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            JobRepository jobRepository,
            AccommodationRepository accommodationRepository
    ) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.jobRepository = jobRepository;
        this.accommodationRepository = accommodationRepository;
    }

    public TransitRouteResponse searchCommuteRoute(CommuteRouteRequest request) {
        if (!StringUtils.hasText(request.getAccommodationId())) {
            throw new IllegalArgumentException("accommodationId is required.");
        }
        if (!StringUtils.hasText(request.getJobId())) {
            throw new IllegalArgumentException("jobId is required.");
        }

        Accommodation accommodation = accommodationRepository.findById(request.getAccommodationId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown accommodationId: " + request.getAccommodationId()));
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown jobId: " + request.getJobId()));

        TransitAnchorResponse origin = accommodationAnchor(accommodation);
        TransitAnchorResponse destination = jobAnchor(job);

        TransitRouteRequest routeRequest = new TransitRouteRequest(
                "ACCOMMODATION",
                accommodation.getId(),
                "JOB",
                job.getId(),
                accommodation.getName(),
                job.getTitle(),
                accommodation.getLatitude(),
                accommodation.getLongitude(),
                job.getLatitude(),
                job.getLongitude(),
                request.getSort(),
                request.getSearchType(),
                request.getPathType(),
                request.getLanguage()
        );

        return searchTransitRoutes(routeRequest, origin, destination);
    }

    public TransitRouteResponse searchTransitRoutes(TransitRouteRequest request) {
        TransitAnchorResponse origin = resolveAnchor(
                request.getStartAnchorType(),
                request.getStartAnchorId(),
                request.getStartName(),
                request.getStartLatitude(),
                request.getStartLongitude()
        );
        TransitAnchorResponse destination = resolveAnchor(
                request.getEndAnchorType(),
                request.getEndAnchorId(),
                request.getEndName(),
                request.getEndLatitude(),
                request.getEndLongitude()
        );

        return searchTransitRoutes(request, origin, destination);
    }

    private TransitRouteResponse searchTransitRoutes(
            TransitRouteRequest request,
            TransitAnchorResponse origin,
            TransitAnchorResponse destination
    ) {
        requireOdsayKey();
        validateCoordinates(origin, "origin");
        validateCoordinates(destination, "destination");

        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/searchPubTransPathT")
                .queryParam("SX", origin.getLongitude())
                .queryParam("SY", origin.getLatitude())
                .queryParam("EX", destination.getLongitude())
                .queryParam("EY", destination.getLatitude())
                .queryParam("OPT", request.getSort() == null ? 0 : request.getSort())
                .queryParam("SearchType", request.getSearchType() == null ? 0 : request.getSearchType())
                .queryParam("SearchPathType", request.getPathType() == null ? 0 : request.getPathType())
                .queryParam("lang", request.getLanguage() == null ? 0 : request.getLanguage())
                .queryParam("output", "json")
                .queryParam("apiKey", apiKey)
                .build()
                .encode()
                .toUri();

        try {
            String body = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(String.class);

            JsonNode raw = objectMapper.readTree(body);

            // ODsay 에러도 raw에 담아 200으로 반환 — 프론트에서 error 필드 확인
            List<TransitRouteOptionResponse> routes = raw.path("error").isMissingNode()
                    ? parseRoutes(raw.path("result").path("path"))
                    : List.of();
            return TransitRouteResponse.builder()
                    .provider("ODSAY")
                    .origin(origin)
                    .destination(destination)
                    .routes(routes)
                    .notices(List.of(
                            "Kakao Map is used for rendering and coordinate picking.",
                            "ODsay searchPubTransPathT is used for public-transit routing."
                    ))
                    .raw(raw)
                    .build();
        } catch (RestClientException e) {
            log.error("ODsay route API call failed: {}", e.getMessage());
            throw new RuntimeException("ODsay route API call failed.");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("ODsay route response parsing failed: {}", e.getMessage());
            throw new RuntimeException("ODsay route response parsing failed.");
        }
    }

    private TransitAnchorResponse resolveAnchor(
            String anchorType,
            String anchorId,
            String fallbackName,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        if (StringUtils.hasText(anchorType) && StringUtils.hasText(anchorId)) {
            String normalizedType = anchorType.trim().toUpperCase();
            if ("JOB".equals(normalizedType)) {
                return jobRepository.findById(anchorId)
                        .map(this::jobAnchor)
                        .orElseThrow(() -> new IllegalArgumentException("Unknown jobId: " + anchorId));
            }
            if ("ACCOMMODATION".equals(normalizedType)) {
                return accommodationRepository.findById(anchorId)
                        .map(this::accommodationAnchor)
                        .orElseThrow(() -> new IllegalArgumentException("Unknown accommodationId: " + anchorId));
            }
            throw new IllegalArgumentException("Unsupported anchorType: " + anchorType);
        }

        return TransitAnchorResponse.builder()
                .type("COORDINATE")
                .name(fallbackName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    private TransitAnchorResponse accommodationAnchor(Accommodation accommodation) {
        return TransitAnchorResponse.builder()
                .type("ACCOMMODATION")
                .id(accommodation.getId())
                .name(accommodation.getName())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .build();
    }

    private TransitAnchorResponse jobAnchor(Job job) {
        return TransitAnchorResponse.builder()
                .type("JOB")
                .id(job.getId())
                .name(job.getTitle())
                .latitude(job.getLatitude())
                .longitude(job.getLongitude())
                .build();
    }

    private void requireOdsayKey() {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalArgumentException("ODsay API key is not configured. Set external.odsay.api-key or ODSAY_API_KEY.");
        }
    }

    private void validateCoordinates(TransitAnchorResponse anchor, String label) {
        if (anchor.getLatitude() == null || anchor.getLongitude() == null) {
            throw new IllegalArgumentException(label + " latitude and longitude are required.");
        }
    }

    private List<TransitRouteOptionResponse> parseRoutes(JsonNode paths) {
        List<TransitRouteOptionResponse> routes = new ArrayList<>();
        if (!paths.isArray()) {
            return routes;
        }

        for (int i = 0; i < paths.size(); i++) {
            JsonNode path = paths.get(i);
            JsonNode info = path.path("info");
            routes.add(TransitRouteOptionResponse.builder()
                    .index(i)
                    .pathType(intOf(path, "pathType"))
                    .pathTypeName(pathTypeName(intOf(path, "pathType")))
                    .totalTime(intOf(info, "totalTime"))
                    .payment(intOf(info, "payment"))
                    .totalWalkTime(firstIntOf(info, "totalWalkTime", "totalWalk"))
                    .totalDistance(firstIntOf(info, "totalDistance", "trafficDistance"))
                    .busTransitCount(intOf(info, "busTransitCount"))
                    .subwayTransitCount(intOf(info, "subwayTransitCount"))
                    .firstStartStation(textOf(info, "firstStartStation"))
                    .lastEndStation(textOf(info, "lastEndStation"))
                    .mapObject(textOf(info, "mapObj"))
                    .subPaths(parseSubPaths(path.path("subPath")))
                    .build());
        }

        return routes;
    }

    private List<TransitSubPathResponse> parseSubPaths(JsonNode subPaths) {
        List<TransitSubPathResponse> result = new ArrayList<>();
        if (!subPaths.isArray()) {
            return result;
        }

        for (JsonNode subPath : subPaths) {
            int trafficType = intOf(subPath, "trafficType");
            result.add(TransitSubPathResponse.builder()
                    .trafficType(trafficType)
                    .trafficTypeName(trafficTypeName(trafficType))
                    .sectionTime(intOf(subPath, "sectionTime"))
                    .distance(intOf(subPath, "distance"))
                    .stationCount(intOf(subPath, "stationCount"))
                    .startName(firstTextOf(subPath, "startName", "startStationName"))
                    .endName(firstTextOf(subPath, "endName", "endStationName"))
                    .lanes(parseLaneNames(subPath.path("lane")))
                    .build());
        }

        return result;
    }

    private List<String> parseLaneNames(JsonNode lanes) {
        List<String> result = new ArrayList<>();
        if (!lanes.isArray()) {
            return result;
        }

        for (JsonNode lane : lanes) {
            String name = firstTextOf(lane, "name", "busNo");
            if (StringUtils.hasText(name)) {
                result.add(name);
            }
        }
        return result;
    }

    private String pathTypeName(int pathType) {
        return switch (pathType) {
            case 1 -> "SUBWAY";
            case 2 -> "BUS";
            case 3 -> "BUS_SUBWAY";
            default -> "UNKNOWN";
        };
    }

    private String trafficTypeName(int trafficType) {
        return switch (trafficType) {
            case 1 -> "SUBWAY";
            case 2 -> "BUS";
            case 3 -> "WALK";
            default -> "UNKNOWN";
        };
    }

    private String textOf(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    private String firstTextOf(JsonNode node, String... fields) {
        for (String field : fields) {
            String value = textOf(node, field);
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private int intOf(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? 0 : value.asInt();
    }

    private int firstIntOf(JsonNode node, String... fields) {
        for (String field : fields) {
            JsonNode value = node.get(field);
            if (value != null && !value.isNull()) {
                return value.asInt();
            }
        }
        return 0;
    }
}
