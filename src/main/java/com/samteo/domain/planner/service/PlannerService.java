package com.samteo.domain.planner.service;

import com.samteo.domain.planner.dto.request.BudgetSimulationRequest;
import com.samteo.domain.planner.dto.request.PlannerCreateRequest;
import com.samteo.domain.planner.dto.response.AccommodationResponse;
import com.samteo.domain.planner.dto.response.BudgetSimulationResponse;
import com.samteo.domain.planner.dto.response.CalendarEventResponse;
import com.samteo.domain.planner.dto.response.JobResponse;
import com.samteo.domain.planner.dto.response.MapProviderResponse;
import com.samteo.domain.planner.dto.response.PlannerBootstrapResponse;
import com.samteo.domain.planner.dto.response.PlannerResponse;
import com.samteo.domain.planner.entity.Accommodation;
import com.samteo.domain.planner.entity.Job;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import com.samteo.domain.region.dto.response.RegionResponse;
import com.samteo.domain.region.entity.Region;
import com.samteo.domain.region.repository.RegionRepository;
import com.samteo.domain.tourapi.dto.response.TourContentResponse;
import com.samteo.domain.tourapi.service.TourApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private static final int DEFAULT_TRANSPORT_COST = 90000;
    private static final int DEFAULT_FOOD_COST = 220000;
    private static final int DEFAULT_EXTRA_COST = 70000;

    private final RegionRepository regionRepository;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;
    private final TourApiService tourApiService;

    private static final Map<String, String> LEGAL_DONG_REGION_CODES = Map.ofEntries(
            Map.entry("서울", "11"), Map.entry("부산", "26"), Map.entry("대구", "27"),
            Map.entry("인천", "28"), Map.entry("광주", "29"), Map.entry("대전", "30"),
            Map.entry("울산", "31"), Map.entry("세종", "36"), Map.entry("경기", "41"),
            Map.entry("충북", "43"), Map.entry("충남", "44"), Map.entry("전남", "46"),
            Map.entry("경북", "47"), Map.entry("경남", "48"), Map.entry("제주", "50"),
            Map.entry("강원", "51"), Map.entry("전북", "52")
    );

    @Transactional(readOnly = true)
    public PlannerBootstrapResponse getBootstrapData() {
        return PlannerBootstrapResponse.builder()
                .regions(regionRepository.findAll().stream().map(RegionResponse::from).toList())
                .jobs(getJobs(null, null))
                .accommodations(getAccommodations(null))
                .mapProvider(getMapProvider())
                .build();
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobs(String regionId, String cityId) {
        List<Job> jobs;
        if (regionId != null) {
            jobs = jobRepository.findByRegionId(regionId);
        } else if (cityId != null) {
            jobs = jobRepository.findByCityId(cityId);
        } else {
            jobs = jobRepository.findAll();
        }
        return jobs.stream()
                .map(JobResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAccommodations(String regionId) {
        List<Region> regions = regionRepository.findAll();
        List<String> regionCodes = regionId == null
                ? regions.stream()
                    .map(Region::getName)
                    .map(this::legalDongRegionCode)
                    .filter(code -> code != null)
                    .distinct()
                    .toList()
                : regionRepository.findById(regionId)
                    .map(Region::getName)
                    .map(this::legalDongRegionCode)
                    .map(List::of)
                    .orElse(List.of());

        Map<Long, TourContentResponse> uniqueStays = new LinkedHashMap<>();
        regionCodes.forEach(code -> tourApiService.getStays(code, 100, 1)
                .forEach(stay -> uniqueStays.putIfAbsent(stay.getContentId(), stay)));

        return uniqueStays.values().stream()
                .map(stay -> toApiAccommodation(stay, regions))
                .filter(response -> regionId == null || regionId.equals(response.getRegionId()))
                .toList();
    }

    private String legalDongRegionCode(String regionName) {
        if (regionName == null) return null;
        return LEGAL_DONG_REGION_CODES.entrySet().stream()
                .filter(entry -> regionName.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private AccommodationResponse toApiAccommodation(TourContentResponse stay, List<Region> regions) {
        String address = String.join(" ",
                stay.getAddr1() == null ? "" : stay.getAddr1(),
                stay.getAddr2() == null ? "" : stay.getAddr2()).trim();
        Region matchedRegion = regions.stream()
                .filter(region -> addressMatchesRegion(address, region.getName()))
                .findFirst()
                .orElse(null);
        String district = matchedRegion == null
                ? (stay.getAddr2() == null ? "지역 미분류" : stay.getAddr2())
                : matchedRegion.getName();

        return AccommodationResponse.builder()
                .id(String.valueOf(stay.getContentId()))
                .name(stay.getTitle())
                .regionId(matchedRegion == null ? null : matchedRegion.getId())
                .district(district)
                .address(address)
                .monthlyPrice(null)
                .price(null)
                .deposit(null)
                .distanceKm(null)
                .commuteMinutes(null)
                .location(address)
                .pos("월 비용 미제공")
                .posType("neg")
                .bg(stay.getFirstImage() == null || stay.getFirstImage().isBlank()
                        ? "linear-gradient(135deg,#1e3a5f,#2d5a8e)"
                        : "url('" + stay.getFirstImage() + "') center/cover")
                .latitude(decimalOf(stay.getMapy()))
                .longitude(decimalOf(stay.getMapx()))
                .lat(decimalOf(stay.getMapy()))
                .lng(decimalOf(stay.getMapx()))
                .tags(List.of())
                .source("TOUR_API_SEARCH_STAY2")
                .imageUrl(stay.getFirstImage())
                .build();
    }

    private boolean addressMatchesRegion(String address, String regionName) {
        if (address == null || regionName == null) return false;
        String[] parts = regionName.split(" ");
        return java.util.Arrays.stream(parts).allMatch(address::contains);
    }

    private BigDecimal decimalOf(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    public MapProviderResponse getMapProvider() {
        return MapProviderResponse.builder()
                .provider("KAKAO")
                .javascriptSdkUrl("https://dapi.kakao.com/v2/maps/sdk.js")
                .enabledFeatures(List.of("marker", "route-preview", "place-search", "public-transit-routing"))
                .integrationNotes(List.of(
                        "Kakao Map should render the map and provide picked coordinates.",
                        "ODsay route API is called by the backend so the ODsay key is not exposed to the frontend.",
                        "Use POST /api/planner/transit-routes for arbitrary anchor coordinates.",
                        "Use POST /api/planner/commute-route for accommodation-to-job routing."
                ))
                .build();
    }

    public BudgetSimulationResponse simulateBudget(BudgetSimulationRequest request) {
        Job job = findJob(request.getJobId());
        Accommodation accommodation = findAccommodation(request.getAccommodationId());
        int transportCost = defaultIfNull(request.getTransportCost(), DEFAULT_TRANSPORT_COST);
        int foodCost = defaultIfNull(request.getFoodCost(), DEFAULT_FOOD_COST);
        int extraCost = defaultIfNull(request.getExtraCost(), DEFAULT_EXTRA_COST);
        int balance = job.getMonthlySalary() - accommodation.getMonthlyPrice() - transportCost - foodCost - extraCost;

        return BudgetSimulationResponse.builder()
                .income(job.getMonthlySalary())
                .housingCost(accommodation.getMonthlyPrice())
                .transportCost(transportCost)
                .foodCost(foodCost)
                .extraCost(extraCost)
                .balance(balance)
                .notes(List.of(
                        "Transport and food costs use user input first.",
                        "Recommended public-transit routes are available through the Kakao/ODsay routing endpoints."
                ))
                .build();
    }

    public PlannerResponse createPlanner(PlannerCreateRequest request) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown regionId: " + request.getRegionId()));
        List<Job> jobs = request.getJobIds().stream().map(this::findJob).toList();
        if (jobs.isEmpty()) {
            throw new IllegalArgumentException("At least one jobId is required.");
        }

        Accommodation accommodation = findAccommodation(request.getAccommodationId());
        LocalDate startDate = request.getStartDate() == null ? LocalDate.now() : request.getStartDate();
        int months = defaultIfNull(request.getMonths(), 1);

        BudgetSimulationResponse budget = simulateBudget(new BudgetSimulationRequest(
                jobs.get(0).getId(),
                accommodation.getId(),
                request.getTransportCost(),
                request.getFoodCost(),
                request.getExtraCost(),
                true
        ));

        String plannerId = UUID.randomUUID().toString();
        return PlannerResponse.builder()
                .plannerId(plannerId)
                .regionName(region.getName())
                .jobNames(jobs.stream().map(Job::getTitle).toList())
                .accommodationName(accommodation.getName())
                .startDate(startDate)
                .endDate(startDate.plusMonths(months).minusDays(1))
                .budget(budget)
                .calendar(createCalendar(startDate, jobs.get(0), accommodation))
                .shareUrl("/planner/share/" + plannerId)
                .build();
    }

    private List<CalendarEventResponse> createCalendar(LocalDate startDate, Job job, Accommodation accommodation) {
        return List.of(
                CalendarEventResponse.builder()
                        .date(startDate)
                        .type("MOVE_IN")
                        .title(accommodation.getName() + " check-in")
                        .description("Start the stay and check into accommodation.")
                        .build(),
                CalendarEventResponse.builder()
                        .date(startDate.plusDays(1))
                        .type("WORK")
                        .title(job.getTitle() + " first work day")
                        .description(job.getWorkingDays())
                        .build(),
                CalendarEventResponse.builder()
                        .date(startDate.plusDays(6))
                        .type("REST")
                        .title("Local exploration day")
                        .description("Check hot places and daily convenience spots.")
                        .build(),
                CalendarEventResponse.builder()
                        .date(startDate.plusDays(13))
                        .type("BUDGET_CHECK")
                        .title("Mid-stay budget check")
                        .description("Enter actual transport, food, and extra costs.")
                        .build()
        );
    }

    private Job findJob(String jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown jobId: " + jobId));
    }

    private Accommodation findAccommodation(String accommodationId) {
        return accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown accommodationId: " + accommodationId));
    }

    private int defaultIfNull(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }
}
