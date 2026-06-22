package com.samteo.domain.tourapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.tourapi.dto.response.TourContentResponse;
import com.samteo.domain.tourapi.dto.response.TourDetailCommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TourApiService {

    private static final Duration LIST_CACHE_TTL  = Duration.ofHours(1);
    private static final Duration DETAIL_CACHE_TTL = Duration.ofHours(6);
    private static final Duration CONNECT_TIMEOUT  = Duration.ofSeconds(3);
    private static final Duration READ_TIMEOUT     = Duration.ofSeconds(5);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private final Map<String, CacheEntry<List<TourContentResponse>>> spotsCache     = new ConcurrentHashMap<>();
    private final Map<String, CacheEntry<List<TourContentResponse>>> festivalsCache  = new ConcurrentHashMap<>();
    private final Map<String, CacheEntry<List<TourContentResponse>>> staysCache      = new ConcurrentHashMap<>();
    private final Map<Long,   CacheEntry<TourDetailCommonResponse>>  detailCommonCache = new ConcurrentHashMap<>();
    private final Map<String, CacheEntry<Map<String, String>>>       detailIntroCache  = new ConcurrentHashMap<>();

    @Value("${external.tourapi.api-key}")
    private String serviceKey;

    @Value("${external.tourapi.base-url}")
    private String baseUrl;

    public TourApiService(RestClient.Builder builder, ObjectMapper objectMapper) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        this.restClient   = builder.requestFactory(factory).build();
        this.objectMapper = objectMapper;
    }

    // ------------------------------------------------------------------ //
    //  관광지 목록 (areaBasedList2)
    // ------------------------------------------------------------------ //

    public List<TourContentResponse> getSpots(Integer areaCode, Integer contentTypeId,
                                              int numOfRows, int pageNo) {
        String key = "spots:" + areaCode + ":" + contentTypeId + ":" + numOfRows + ":" + pageNo;
        CacheEntry<List<TourContentResponse>> cached = spotsCache.get(key);
        if (cached != null && !cached.isExpired()) {
            return cached.get();
        }

        UriComponentsBuilder ub = UriComponentsBuilder.fromUriString(baseUrl + "/areaBasedList2")
                .queryParam("serviceKey", encodedKey())
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "samteo")
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("_type", "json");
        if (areaCode    != null) ub.queryParam("areaCode",      areaCode);
        if (contentTypeId != null) ub.queryParam("contentTypeId", contentTypeId);

        List<TourContentResponse> result = fetchContentList(ub.build(true).toUri());
        spotsCache.put(key, new CacheEntry<>(result, LIST_CACHE_TTL));
        return result;
    }

    // ------------------------------------------------------------------ //
    //  축제 목록 (searchFestival2)
    // ------------------------------------------------------------------ //

    public List<TourContentResponse> getFestivals(String eventStartDate, Integer areaCode,
                                                  int numOfRows, int pageNo) {
        String key = "festivals:" + eventStartDate + ":" + areaCode + ":" + numOfRows + ":" + pageNo;
        CacheEntry<List<TourContentResponse>> cached = festivalsCache.get(key);
        if (cached != null && !cached.isExpired()) {
            return cached.get();
        }

        UriComponentsBuilder ub = UriComponentsBuilder.fromUriString(baseUrl + "/searchFestival2")
                .queryParam("serviceKey", encodedKey())
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "samteo")
                .queryParam("eventStartDate", eventStartDate)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("_type", "json");
        if (areaCode != null) ub.queryParam("areaCode", areaCode);

        List<TourContentResponse> result = fetchContentList(ub.build(true).toUri());
        festivalsCache.put(key, new CacheEntry<>(result, LIST_CACHE_TTL));
        return result;
    }

    public List<TourContentResponse> getStays(String legalDongRegionCode,
                                               int numOfRows, int pageNo) {
        String key = "stays:" + legalDongRegionCode + ":" + numOfRows + ":" + pageNo;
        CacheEntry<List<TourContentResponse>> cached = staysCache.get(key);
        if (cached != null && !cached.isExpired()) {
            return cached.get();
        }

        UriComponentsBuilder ub = UriComponentsBuilder.fromUriString(baseUrl + "/searchStay2")
                .queryParam("serviceKey", encodedKey())
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "samteo")
                .queryParam("lDongRegnCd", legalDongRegionCode)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("_type", "json");

        List<TourContentResponse> result = fetchContentList(ub.build(true).toUri());
        staysCache.put(key, new CacheEntry<>(result, LIST_CACHE_TTL));
        return result;
    }

    // ------------------------------------------------------------------ //
    //  공통 상세정보 (detailCommon2)
    // ------------------------------------------------------------------ //

    public TourDetailCommonResponse getDetailCommon(long contentId) {
        CacheEntry<TourDetailCommonResponse> cached = detailCommonCache.get(contentId);
        if (cached != null && !cached.isExpired()) {
            return cached.get();
        }

        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/detailCommon2")
                .queryParam("serviceKey", encodedKey())
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "samteo")
                .queryParam("contentId", contentId)
                .queryParam("defaultYN", "Y")
                .queryParam("firstImageYN", "Y")
                .queryParam("addrinfoYN", "Y")
                .queryParam("overviewYN", "Y")
                .queryParam("_type", "json")
                .build(true).toUri();

        try {
            String body = get(uri);
            JsonNode item = singleItem(objectMapper.readTree(body));
            if (item == null) return null;

            TourDetailCommonResponse response = TourDetailCommonResponse.builder()
                    .contentId(longOf(item, "contentid"))
                    .contentTypeId(intOf(item, "contenttypeid"))
                    .title(textOf(item, "title"))
                    .tel(textOf(item, "tel"))
                    .telName(textOf(item, "telname"))
                    .homepage(textOf(item, "homepage"))
                    .addr1(textOf(item, "addr1"))
                    .addr2(textOf(item, "addr2"))
                    .zipcode(textOf(item, "zipcode"))
                    .mapx(doubleOf(item, "mapx"))
                    .mapy(doubleOf(item, "mapy"))
                    .firstImage(textOf(item, "firstimage"))
                    .firstImage2(textOf(item, "firstimage2"))
                    .overview(textOf(item, "overview"))
                    .createdTime(textOf(item, "createdtime"))
                    .modifiedTime(textOf(item, "modifiedtime"))
                    .build();

            detailCommonCache.put(contentId, new CacheEntry<>(response, DETAIL_CACHE_TTL));
            return response;

        } catch (Exception e) {
            log.error("detailCommon2 API call failed contentId={}: {}", contentId, e.getMessage());
            throw new RuntimeException("관광정보 상세 조회에 실패했습니다.");
        }
    }

    // ------------------------------------------------------------------ //
    //  소개 상세정보 (detailIntro2) - 타입별 필드가 달라 Map으로 반환
    // ------------------------------------------------------------------ //

    public Map<String, String> getDetailIntro(long contentId, int contentTypeId) {
        String key = contentId + ":" + contentTypeId;
        CacheEntry<Map<String, String>> cached = detailIntroCache.get(key);
        if (cached != null && !cached.isExpired()) {
            return cached.get();
        }

        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/detailIntro2")
                .queryParam("serviceKey", encodedKey())
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "samteo")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("_type", "json")
                .build(true).toUri();

        try {
            String body = get(uri);
            JsonNode item = singleItem(objectMapper.readTree(body));
            if (item == null) return Map.of();

            Map<String, String> fields = new LinkedHashMap<>();
            item.fields().forEachRemaining(e -> {
                if (!e.getValue().isNull()) {
                    fields.put(e.getKey(), e.getValue().asText());
                }
            });

            detailIntroCache.put(key, new CacheEntry<>(fields, DETAIL_CACHE_TTL));
            return fields;

        } catch (Exception e) {
            log.error("detailIntro2 API call failed contentId={}: {}", contentId, e.getMessage());
            throw new RuntimeException("관광정보 소개 조회에 실패했습니다.");
        }
    }

    // ------------------------------------------------------------------ //
    //  내부 유틸
    // ------------------------------------------------------------------ //

    private String encodedKey() {
        return URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
    }

    private List<TourContentResponse> fetchContentList(URI uri) {
        try {
            log.debug("TourAPI 호출 URL: {}", uri);
            String body = get(uri);
            JsonNode root = objectMapper.readTree(body);
            // data.go.kr 오류 응답 처리
            JsonNode resultCode = root.path("response").path("header").path("resultCode");
            if (!resultCode.isMissingNode() && !"0000".equals(resultCode.asText())) {
                String resultMessage = root.path("response").path("header").path("resultMsg").asText();
                throw new IllegalStateException("TourAPI error response: " + resultMessage);
            }
            JsonNode items = root.path("response").path("body").path("items").path("item");
            return List.copyOf(parseContentItems(items));
        } catch (Exception e) {
            log.error("TourAPI list call failed: {}", e.getMessage());
            throw new RuntimeException("TourAPI list call failed.", e);
        }
    }

    private String get(URI uri) {
        return restClient.get().uri(uri).retrieve().body(String.class);
    }

    private List<TourContentResponse> parseContentItems(JsonNode items) {
        List<TourContentResponse> result = new ArrayList<>();
        if (items.isArray()) {
            items.forEach(n -> result.add(toContentResponse(n)));
        } else if (!items.isMissingNode() && !items.isNull()) {
            result.add(toContentResponse(items));
        }
        return result;
    }

    private TourContentResponse toContentResponse(JsonNode n) {
        return TourContentResponse.builder()
                .contentId(longOf(n, "contentid"))
                .contentTypeId(intOf(n, "contenttypeid"))
                .title(textOf(n, "title"))
                .addr1(textOf(n, "addr1"))
                .addr2(textOf(n, "addr2"))
                .areaCode(intOf(n, "areacode"))
                .sigunguCode(intOf(n, "sigungucode"))
                .mapx(doubleOf(n, "mapx"))
                .mapy(doubleOf(n, "mapy"))
                .firstImage(textOf(n, "firstimage"))
                .firstImage2(textOf(n, "firstimage2"))
                .tel(textOf(n, "tel"))
                .cat1(textOf(n, "cat1"))
                .cat2(textOf(n, "cat2"))
                .cat3(textOf(n, "cat3"))
                .createdTime(textOf(n, "createdtime"))
                .modifiedTime(textOf(n, "modifiedtime"))
                .eventStartDate(textOf(n, "eventstartdate"))
                .eventEndDate(textOf(n, "eventenddate"))
                .build();
    }

    private JsonNode singleItem(JsonNode root) {
        JsonNode item = root.path("response").path("body").path("items").path("item");
        if (item.isMissingNode() || item.isNull()) return null;
        return item.isArray() ? item.get(0) : item;
    }

    private String textOf(JsonNode n, String field) {
        JsonNode v = n.get(field);
        return (v != null && !v.isNull()) ? v.asText() : null;
    }

    private Integer intOf(JsonNode n, String field) {
        String v = textOf(n, field);
        if (v == null || v.isBlank()) return null;
        try { return Integer.parseInt(v.trim()); } catch (NumberFormatException e) { return null; }
    }

    private Long longOf(JsonNode n, String field) {
        String v = textOf(n, field);
        if (v == null || v.isBlank()) return null;
        try { return Long.parseLong(v.trim()); } catch (NumberFormatException e) { return null; }
    }

    private Double doubleOf(JsonNode n, String field) {
        String v = textOf(n, field);
        if (v == null || v.isBlank()) return null;
        try { return Double.parseDouble(v.trim()); } catch (NumberFormatException e) { return null; }
    }

    // ------------------------------------------------------------------ //
    //  캐시 내부 클래스
    // ------------------------------------------------------------------ //

    private static class CacheEntry<T> {
        private final T data;
        private final long expiresAtMillis;

        CacheEntry(T data, Duration ttl) {
            this.data = data;
            this.expiresAtMillis = System.currentTimeMillis() + ttl.toMillis();
        }

        T get() { return data; }
        boolean isExpired() { return System.currentTimeMillis() >= expiresAtMillis; }
    }
}
