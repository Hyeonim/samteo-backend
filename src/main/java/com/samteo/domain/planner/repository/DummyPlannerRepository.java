package com.samteo.domain.planner.repository;

import com.samteo.domain.planner.entity.Accommodation;
import com.samteo.domain.planner.entity.Job;
import com.samteo.domain.region.entity.Region;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class DummyPlannerRepository {

    private final List<Region> regions = List.of(
            Region.builder()
                    .id("junggu")
                    .name("Jung-gu")
                    .summary("Central Daegu district with strong access to jobs and cultural spots.")
                    .hotPlaceScore(95)
                    .housingCostScore(62)
                    .centerLatitude(new BigDecimal("35.8693"))
                    .centerLongitude(new BigDecimal("128.6062"))
                    .tags(List.of("hot-place", "downtown", "transit"))
                    .recommendationReasons(List.of("Many short-term jobs near Dongseong-ro.", "Good fit for walking and transit-based living."))
                    .build(),
            Region.builder()
                    .id("donggu")
                    .name("Dong-gu")
                    .summary("Transport-friendly district near Dongdaegu Station and Palgong Mountain.")
                    .hotPlaceScore(82)
                    .housingCostScore(74)
                    .centerLatitude(new BigDecimal("35.8867"))
                    .centerLongitude(new BigDecimal("128.6356"))
                    .tags(List.of("transit", "tourism", "nature"))
                    .recommendationReasons(List.of("Excellent access around Dongdaegu Station.", "Good match for tourism-related jobs."))
                    .build(),
            Region.builder()
                    .id("suseong")
                    .name("Suseong-gu")
                    .summary("Stable residential district with strong lifestyle infrastructure.")
                    .hotPlaceScore(88)
                    .housingCostScore(48)
                    .centerLatitude(new BigDecimal("35.8582"))
                    .centerLongitude(new BigDecimal("128.6306"))
                    .tags(List.of("housing", "cafe", "lake"))
                    .recommendationReasons(List.of("Comfortable rest routes near Suseong Lake.", "Dense everyday convenience facilities."))
                    .build(),
            Region.builder()
                    .id("dalseo")
                    .name("Dalseo-gu")
                    .summary("Cost-effective district with access to industrial and commercial areas.")
                    .hotPlaceScore(76)
                    .housingCostScore(86)
                    .centerLatitude(new BigDecimal("35.8299"))
                    .centerLongitude(new BigDecimal("128.5327"))
                    .tags(List.of("budget", "living", "industry"))
                    .recommendationReasons(List.of("Good option for reducing stay costs.", "Useful for jobs near Seongseo Industrial Complex."))
                    .build(),
            Region.builder()
                    .id("bukgu")
                    .name("Buk-gu")
                    .summary("Youth-friendly district mixed with university areas and local commerce.")
                    .hotPlaceScore(80)
                    .housingCostScore(78)
                    .centerLatitude(new BigDecimal("35.8859"))
                    .centerLongitude(new BigDecimal("128.5829"))
                    .tags(List.of("university", "commerce", "budget"))
                    .recommendationReasons(List.of("Part-time jobs around university areas.", "Balanced living cost."))
                    .build()
    );

    private final List<Job> jobs = List.of(
            Job.builder()
                    .id("job-001")
                    .title("Dongseong-ro Bakery Cafe Staff")
                    .company("Samteo Bakery")
                    .regionId("junggu")
                    .district("Jung-gu")
                    .category("Food and Beverage")
                    .employmentType("Part-time")
                    .monthlySalary(1600000)
                    .workingDays("Mon-Fri 10:00-17:00")
                    .commuteMinutes(22)
                    .latitude(new BigDecimal("35.8698"))
                    .longitude(new BigDecimal("128.5940"))
                    .tags(List.of("beginner", "cafe", "downtown"))
                    .build(),
            Job.builder()
                    .id("job-002")
                    .title("Palgong Mountain Tourism Staff")
                    .company("Daegu Travel Lounge")
                    .regionId("donggu")
                    .district("Dong-gu")
                    .category("Tourism")
                    .employmentType("Contract")
                    .monthlySalary(2100000)
                    .workingDays("Wed-Sun 09:00-18:00")
                    .commuteMinutes(48)
                    .latitude(new BigDecimal("35.9913"))
                    .longitude(new BigDecimal("128.6958"))
                    .tags(List.of("weekend", "tourism", "language-plus"))
                    .build(),
            Job.builder()
                    .id("job-003")
                    .title("Suseong Lake Convenience Store")
                    .company("Samteo Retail")
                    .regionId("suseong")
                    .district("Suseong-gu")
                    .category("Store")
                    .employmentType("Part-time")
                    .monthlySalary(800000)
                    .workingDays("3 nights per week")
                    .commuteMinutes(35)
                    .latitude(new BigDecimal("35.8265"))
                    .longitude(new BigDecimal("128.6177"))
                    .tags(List.of("night", "station", "short-term"))
                    .build(),
            Job.builder()
                    .id("job-004")
                    .title("Seongseo Logistics Picking")
                    .company("Seongseo Fulfillment")
                    .regionId("dalseo")
                    .district("Dalseo-gu")
                    .category("Logistics")
                    .employmentType("Short contract")
                    .monthlySalary(1850000)
                    .workingDays("Mon-Fri 08:00-17:00")
                    .commuteMinutes(42)
                    .latitude(new BigDecimal("35.8372"))
                    .longitude(new BigDecimal("128.4888"))
                    .tags(List.of("meal-support", "logistics", "short-term"))
                    .build()
    );

    private final List<Accommodation> accommodations = List.of(
            Accommodation.builder()
                    .id("acc-001")
                    .name("Daegu Downtown Hostel")
                    .regionId("junggu")
                    .district("Jung-gu")
                    .address("12 Jungang-daero 1-gil")
                    .monthlyPrice(450000)
                    .deposit(100000)
                    .distanceKm(new BigDecimal("1.2"))
                    .commuteMinutes(18)
                    .latitude(new BigDecimal("35.8714"))
                    .longitude(new BigDecimal("128.6014"))
                    .tags(List.of("shared-kitchen", "downtown", "short-term"))
                    .build(),
            Accommodation.builder()
                    .id("acc-002")
                    .name("Dongdaegu Stay")
                    .regionId("donggu")
                    .district("Dong-gu")
                    .address("550 Dongdaegu-ro")
                    .monthlyPrice(520000)
                    .deposit(200000)
                    .distanceKm(new BigDecimal("2.8"))
                    .commuteMinutes(35)
                    .latitude(new BigDecimal("35.8797"))
                    .longitude(new BigDecimal("128.6289"))
                    .tags(List.of("station", "private-room", "transit"))
                    .build(),
            Accommodation.builder()
                    .id("acc-003")
                    .name("Suseong Lake One-roomtel")
                    .regionId("suseong")
                    .district("Suseong-gu")
                    .address("24 Suseongmot-gil")
                    .monthlyPrice(620000)
                    .deposit(300000)
                    .distanceKm(new BigDecimal("3.1"))
                    .commuteMinutes(30)
                    .latitude(new BigDecimal("35.8288"))
                    .longitude(new BigDecimal("128.6180"))
                    .tags(List.of("private-room", "lake", "quiet"))
                    .build(),
            Accommodation.builder()
                    .id("acc-004")
                    .name("Seongseo Budget Residence")
                    .regionId("dalseo")
                    .district("Dalseo-gu")
                    .address("72 Seongseogongdan-ro")
                    .monthlyPrice(380000)
                    .deposit(100000)
                    .distanceKm(new BigDecimal("1.9"))
                    .commuteMinutes(24)
                    .latitude(new BigDecimal("35.8423"))
                    .longitude(new BigDecimal("128.4921"))
                    .tags(List.of("budget", "laundry", "industrial-area"))
                    .build()
    );

    public List<Region> findRegions() {
        return regions;
    }

    public Optional<Region> findRegion(String id) {
        return regions.stream().filter(region -> region.getId().equals(id)).findFirst();
    }

    public List<Job> findJobs(String regionId) {
        return jobs.stream()
                .filter(job -> regionId == null || job.getRegionId().equals(regionId))
                .toList();
    }

    public Optional<Job> findJob(String id) {
        return jobs.stream().filter(job -> job.getId().equals(id)).findFirst();
    }

    public List<Accommodation> findAccommodations(String regionId) {
        return accommodations.stream()
                .filter(accommodation -> regionId == null || accommodation.getRegionId().equals(regionId))
                .sorted(Comparator.comparing(Accommodation::getCommuteMinutes))
                .toList();
    }

    public Optional<Accommodation> findAccommodation(String id) {
        return accommodations.stream().filter(accommodation -> accommodation.getId().equals(id)).findFirst();
    }
}
