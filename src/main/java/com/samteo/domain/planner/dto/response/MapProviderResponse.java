package com.samteo.domain.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MapProviderResponse {

    private String provider;
    private String javascriptSdkUrl;
    private List<String> enabledFeatures;
    private List<String> integrationNotes;
}
