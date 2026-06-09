package com.samteo.domain.region.service;

import com.samteo.domain.region.dto.request.RegionRecommendationRequest;
import com.samteo.domain.region.dto.response.RegionResponse;
import com.samteo.domain.region.entity.Region;
import com.samteo.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final SecureRandom random = new SecureRandom();

    @Transactional(readOnly = true)
    public List<RegionResponse> getRegions() {
        return regionRepository.findAll().stream()
                .map(RegionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> recommend(RegionRecommendationRequest request) {
        String option = request == null || request.getOption() == null ? "random" : request.getOption();

        if ("district".equalsIgnoreCase(option) && request.getDistrict() != null) {
            return regionRepository.findAll().stream()
                    .filter(region -> region.getName().equals(request.getDistrict()) || region.getId().equals(request.getDistrict()))
                    .map(RegionResponse::from)
                    .toList();
        }

        if ("needs".equalsIgnoreCase(option)) {
            List<String> needs = request.getNeeds() == null ? List.of() : request.getNeeds();
            return regionRepository.findAll().stream()
                    .sorted(Comparator.comparingInt((Region region) -> scoreNeeds(region.getTags(), needs)).reversed())
                    .limit(3)
                    .map(RegionResponse::from)
                    .toList();
        }

        List<RegionResponse> regions = getRegions();
        return List.of(regions.get(random.nextInt(regions.size())));
    }

    private int scoreNeeds(List<String> tags, List<String> needs) {
        if (needs.isEmpty()) {
            return 0;
        }

        return (int) needs.stream()
                .filter(need -> tags.stream().anyMatch(tag -> tag.contains(need) || need.contains(tag)))
                .count();
    }
}
