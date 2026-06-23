package com.samteo.domain.region.service;

import com.samteo.domain.region.dto.request.RegionRecommendationRequest;
import com.samteo.domain.region.dto.response.RegionResponse;
import com.samteo.domain.region.repository.MetaRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final MetaRegionRepository metaRegionRepository;
    private final SecureRandom random = new SecureRandom();

    @Transactional(readOnly = true)
    public List<RegionResponse> getRegions() {
        return metaRegionRepository.findAllByOrderByIdAsc().stream()
                .map(RegionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> recommend(RegionRecommendationRequest request) {
        String option = request == null || request.getOption() == null ? "random" : request.getOption();

        List<RegionResponse> regions = getRegions();
        if ("district".equalsIgnoreCase(option) && request.getDistrict() != null) {
            return regions.stream()
                    .filter(region -> region.getName().equals(request.getDistrict())
                            || region.getId().equals(request.getDistrict()))
                    .toList();
        }
        if ("needs".equalsIgnoreCase(option)) {
            return regions.stream().limit(3).toList();
        }
        return List.of(regions.get(random.nextInt(regions.size())));
    }
}
