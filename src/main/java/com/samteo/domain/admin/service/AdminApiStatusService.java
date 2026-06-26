package com.samteo.domain.admin.service;

import com.samteo.domain.admin.dto.response.ApiStatusResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AdminApiStatusService {

    private final OkHttpClient httpClient;

    @Value("${external.tourapi.api-key:}")   private String tourApiKey;
    @Value("${external.tourapi.base-url}")   private String tourApiBaseUrl;
    @Value("${external.alio.api-key:}")      private String alioApiKey;
    @Value("${external.alio.base-url}")      private String alioBaseUrl;
    @Value("${external.odsay.api-key:}")     private String odsayApiKey;
    @Value("${external.odsay.base-url:https://api.odsay.com/v1/api}") private String odsayBaseUrl;
    @Value("${external.odsay.origin:https://samteo.org}") private String odsayOrigin;

    public AdminApiStatusService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public List<ApiStatusResponse> checkAll() {
        return List.of(checkTourApi(), checkAlio(), checkOdsay());
    }

    private ApiStatusResponse checkTourApi() {
        if (!StringUtils.hasText(tourApiKey)) return ApiStatusResponse.noKey("TourAPI");
        String url = tourApiBaseUrl + "/areaList2?numOfRows=1&pageNo=1&MobileOS=ETC&MobileApp=samteo&_type=json&serviceKey=" + tourApiKey;
        return doGet("TourAPI", url, null, null);
    }

    private ApiStatusResponse checkAlio() {
        if (!StringUtils.hasText(alioApiKey)) return ApiStatusResponse.noKey("Alio");
        String url = alioBaseUrl + "?apikey=" + alioApiKey;
        String form = "workRegionCode=R3010&pageIndex=1&pageUnit=1";
        return doPost("Alio", url, form);
    }

    private ApiStatusResponse checkOdsay() {
        if (!StringUtils.hasText(odsayApiKey)) return ApiStatusResponse.noKey("ODsay");
        // 서울역 → 강남역 테스트
        String url = odsayBaseUrl + "/searchPubTransPathT?SX=126.9722&SY=37.5547&EX=127.0276&EY=37.4979&output=json&apiKey=" + odsayApiKey;
        return doGet("ODsay", url, "Origin", odsayOrigin);
    }

    private ApiStatusResponse doGet(String name, String url, String headerName, String headerValue) {
        long start = System.currentTimeMillis();
        try {
            Request.Builder builder = new Request.Builder().url(url);
            if (StringUtils.hasText(headerName)) builder.header(headerName, headerValue);
            try (Response response = httpClient.newCall(builder.build()).execute()) {
                long ms = System.currentTimeMillis() - start;
                String body = response.body() != null ? response.body().string() : "";
                if (!response.isSuccessful()) {
                    return ApiStatusResponse.error(name, ms, "HTTP " + response.code());
                }
                if (body.contains("ApiKeyAuthFailed") || body.contains("INVALID_REQUEST_PARAMETER_ERROR")) {
                    return ApiStatusResponse.error(name, ms, "API 키 인증 실패");
                }
                return ApiStatusResponse.ok(name, ms);
            }
        } catch (Exception e) {
            long ms = System.currentTimeMillis() - start;
            log.warn("{} health check failed: {}", name, e.getMessage());
            return ApiStatusResponse.error(name, ms, e.getMessage());
        }
    }

    private ApiStatusResponse doPost(String name, String url, String formBody) {
        long start = System.currentTimeMillis();
        try {
            RequestBody body = RequestBody.create(formBody, MediaType.parse("application/x-www-form-urlencoded"));
            Request request = new Request.Builder().url(url).post(body).build();
            try (Response response = httpClient.newCall(request).execute()) {
                long ms = System.currentTimeMillis() - start;
                if (!response.isSuccessful()) {
                    return ApiStatusResponse.error(name, ms, "HTTP " + response.code());
                }
                return ApiStatusResponse.ok(name, ms);
            }
        } catch (Exception e) {
            long ms = System.currentTimeMillis() - start;
            log.warn("{} health check failed: {}", name, e.getMessage());
            return ApiStatusResponse.error(name, ms, e.getMessage());
        }
    }
}
