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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;


import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OdsayTransitService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;

    @Value("${external.odsay.base-url:https://api.odsay.com/v1/api}")
    private String baseUrl;

    @Value("${external.odsay.api-key:}")
    private String apiKey;

    @Value("${external.odsay.origin:https://samteo.org}")
    private String odsayOrigin;

    public OdsayTransitService(
            ObjectMapper objectMapper,
            JobRepository jobRepository,
            AccommodationRepository accommodationRepository
    ) {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
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

    public JsonNode loadLane(String mapObject) {
        requireOdsayKey();
        if (!StringUtils.hasText(mapObject)) {
            throw new IllegalArgumentException("mapObject is required.");
        }

        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/loadLane")
                .queryParam("mapObject", mapObject)
                .queryParam("output", "json")
                .queryParam("apiKey", apiKey)
                .build()
                .encode()
                .toUri();

        try {
            String body = odsayGet(uri);
            JsonNode raw = objectMapper.readTree(body);
            JsonNode effectiveError = resolveOdsayError(raw.path("error"));
            if (effectiveError != null) {
                int code = effectiveError.path("code").asInt(0);
                String message = effectiveError.path("message").asText(effectiveError.path("msg").asText("ODsay lane search failed."));
                log.warn("ODsay loadLane error — code={}, message={}", code, message);
                throw new IllegalArgumentException("ODsay error: " + message);
            }
            return raw;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("ODsay lane API call failed: {}", e.getMessage());
            throw new RuntimeException("ODsay lane API call failed.");
        }
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
            String body = odsayGet(uri);
            JsonNode raw = objectMapper.readTree(body);
            JsonNode errorNode = raw.path("error");
            JsonNode effectiveError = resolveOdsayError(errorNode);
            if (effectiveError != null) {
                int code = effectiveError.path("code").asInt(0);
                String message = effectiveError.path("message").asText(effectiveError.path("msg").asText("ODsay route search failed."));
                log.warn("ODsay transit error — code={}, message={}", code, message);
                if (code == -98 || code == -99) {
                    return TransitRouteResponse.builder()
                            .provider("ODSAY")
                            .origin(origin)
                            .destination(destination)
                            .routes(List.of())
                            .notices(List.of("해당 구간의 대중교통 경로를 찾을 수 없습니다."))
                            .build();
                }
                throw new IllegalArgumentException("ODsay error: " + message);
            }

            List<TransitRouteOptionResponse> routes = parseRoutes(raw.path("result").path("path"));
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

    /**
     * ODsay 에러 노드를 해석한다.
     * 성공 응답의 "error": false 는 null 반환 (에러 아님).
     * 에러 응답의 "error": [{...}] (배열) 또는 "error": {...} (객체) 는 첫 번째 에러 객체 반환.
     */
    private JsonNode resolveOdsayError(JsonNode errorNode) {
        if (errorNode == null || errorNode.isMissingNode() || errorNode.isNull()) return null;
        if (errorNode.isBoolean()) return errorNode.asBoolean() ? errorNode : null;
        if (errorNode.isArray()) return errorNode.isEmpty() ? null : errorNode.get(0);
        if (errorNode.isObject()) return errorNode;
        return null; // number 0 / false-ish primitive → 에러 아님
    }

    private String odsayGet(URI uri) throws Exception {
        Request request = new Request.Builder()
                .url(uri.toString())
                .header("Origin", odsayOrigin)
                .header("Referer", odsayOrigin + "/")
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("ODsay HTTP " + response.code());
            return response.body().string();
        }
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
