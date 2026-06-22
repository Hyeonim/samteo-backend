package com.samteo.domain.planner.service;

import com.samteo.domain.planner.dto.request.BudgetSimulationRequest;
import com.samteo.domain.planner.dto.request.PlannerCreateRequest;
import com.samteo.domain.planner.dto.response.AccommodationResponse;
import com.samteo.domain.planner.dto.response.BudgetSimulationResponse;
import com.samteo.domain.planner.dto.response.CalendarEventResponse;
import com.samteo.domain.planner.dto.response.JobResponse;
import com.samteo.domain.planner.dto.response.JobPageResponse;
import com.samteo.domain.planner.dto.response.MapProviderResponse;
import com.samteo.domain.planner.dto.response.PlannerBootstrapResponse;
import com.samteo.domain.planner.dto.response.PlannerResponse;
import com.samteo.domain.planner.entity.Accommodation;
import com.samteo.domain.planner.entity.Job;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import com.samteo.domain.region.dto.response.RegionResponse;
import com.samteo.domain.region.entity.Region;
import com.samteo.domain.region.entity.MetaRegion;
import com.samteo.domain.region.repository.MetaRegionRepository;
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

    private static final Map<String, String> ALIO_REGION_CODES = Map.ofEntries(
            Map.entry("서울", "R3010"), Map.entry("인천", "R3011"), Map.entry("대전", "R3012"),
            Map.entry("대구", "R3013"), Map.entry("부산", "R3014"), Map.entry("광주", "R3015"),
            Map.entry("울산", "R3016"), Map.entry("경기", "R3017"), Map.entry("강원", "R3018"),
            Map.entry("충남", "R3019"), Map.entry("충북", "R3020"), Map.entry("경북", "R3021"),
            Map.entry("경남", "R3022"), Map.entry("전남", "R3023"), Map.entry("전북", "R3024"),
            Map.entry("제주", "R3025"), Map.entry("세종", "R3026")
    );

    private final RegionRepository regionRepository;
    private final MetaRegionRepository metaRegionRepository;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;
    private final TourApiService tourApiService;
    private final com.samteo.domain.job.service.JobService externalJobService;

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
        return getJobsPage(regionId, cityId, 1, 100).getItems();
    }

    @Transactional(readOnly = true)
    public JobPageResponse getJobsPage(String regionId, String cityId, int page, int size) {
        String selectedId = regionId != null ? regionId : cityId;
        List<MetaRegion> metaRegions = metaRegionRepository.findAllByOrderByIdAsc();
        MetaRegion selectedRegion = resolveMetaRegion(selectedId, metaRegions);

        String alioRegionCode = selectedRegion == null
                ? null
                : ALIO_REGION_CODES.get(shortRegionName(selectedRegion.getName()));

        com.samteo.domain.job.service.JobService.JobPage externalPage =
                externalJobService.getJobsPage(page, size, alioRegionCode);
        List<JobResponse> items = externalPage.jobs().stream()
                .filter(job -> selectedRegion == null || locationMatchesRegion(job.getLocation(), selectedRegion.getName()))
                .map(job -> toAlioPlannerJob(job, selectedRegion == null
                        ? findMatchingMetaRegion(job.getLocation(), metaRegions)
                        : selectedRegion))
                .toList();
        int totalPages = (int) Math.ceil((double) externalPage.totalCount() / externalPage.size());
        return JobPageResponse.builder()
                .items(items)
                .page(externalPage.page())
                .size(externalPage.size())
                .totalCount(externalPage.totalCount())
                .totalPages(totalPages)
                .hasNext(externalPage.page() < totalPages)
                .build();
    }

    private MetaRegion resolveMetaRegion(String regionId, List<MetaRegion> metaRegions) {
        if (regionId == null || regionId.isBlank()) return null;
        java.util.Optional<Integer> numericId = parseMetaRegionId(regionId);
        if (numericId.isPresent()) {
            return metaRegions.stream().filter(region -> region.getId().equals(numericId.get())).findFirst().orElse(null);
        }
        Map<String, String> legacyNames = Map.ofEntries(
                Map.entry("seoul", "서울"), Map.entry("busan", "부산"), Map.entry("daegu", "대구"),
                Map.entry("incheon", "인천"), Map.entry("gwangju", "광주"), Map.entry("daejeon", "대전"),
                Map.entry("ulsan", "울산"), Map.entry("sejong", "세종"), Map.entry("jeju", "제주")
        );
        String expected = legacyNames.getOrDefault(regionId.toLowerCase(), regionId);
        return metaRegions.stream()
                .filter(region -> shortRegionName(region.getName()).equals(expected))
                .findFirst()
                .orElse(null);
    }

    private MetaRegion findMatchingMetaRegion(String location, List<MetaRegion> metaRegions) {
        return metaRegions.stream()
                .filter(region -> locationMatchesRegion(location, region.getName()))
                .findFirst()
                .orElse(null);
    }

    private JobResponse toAlioPlannerJob(
            com.samteo.domain.job.dto.response.JobResponse job,
            MetaRegion selectedRegion
    ) {
        String regionName = selectedRegion == null ? job.getLocation() : selectedRegion.getName();
        int hourlyWage = job.getHourlyWage() == null ? 0 : job.getHourlyWage();
        int monthlySalary = job.getMonthlySalary() == null ? hourlyWage * 209 : job.getMonthlySalary();
        List<String> tags = java.util.stream.Stream.of(job.getType(), job.getEmploymentType(), job.getEducation())
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .toList();

        return JobResponse.builder()
                .id(job.getId())
                .name(job.getTitle())
                .title(job.getTitle())
                .company(job.getCompany())
                .cityId(selectedRegion == null ? null : String.valueOf(selectedRegion.getId()))
                .cityName(regionName)
                .regionId(selectedRegion == null ? null : String.valueOf(selectedRegion.getId()))
                .district(regionName)
                .region(regionName)
                .address(job.getLocation())
                .type(job.getType())
                .category(job.getType())
                .employmentType(job.getEmploymentType())
                .hourlyWage(hourlyWage)
                .monthlySalary(monthlySalary)
                .salary(monthlySalary)
                .workingDays("공고 상세 확인")
                .commuteMinutes(0)
                .desc(job.getDescription() == null || job.getDescription().isBlank()
                        ? job.getCompany() : job.getDescription())
                .location(job.getLocation())
                .priceLabel(String.format("%,d원", hourlyWage))
                .unit("/시간")
                .sub("기본 최저시급 · 플래너 완성 후 수정 가능")
                .emoji("💼")
                .tags(tags)
                .source("ALIO")
                .sourceUrl(job.getSourceUrl())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .build();
    }

    private boolean locationMatchesRegion(String location, String regionName) {
        if (location == null || regionName == null) return false;
        return location.contains(shortRegionName(regionName));
    }

    private String shortRegionName(String name) {
        return name
                .replace("서울특별시", "서울").replace("부산광역시", "부산")
                .replace("대구광역시", "대구").replace("인천광역시", "인천")
                .replace("광주광역시", "광주").replace("대전광역시", "대전")
                .replace("울산광역시", "울산").replace("세종특별자치시", "세종")
                .replace("경기도", "경기").replace("강원특별자치도", "강원")
                .replace("충청북도", "충북").replace("충청남도", "충남")
                .replace("전북특별자치도", "전북").replace("전라남도", "전남")
                .replace("경상북도", "경북").replace("경상남도", "경남")
                .replace("제주특별자치도", "제주");
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAccommodations(String regionId) {
        List<Region> regions = regionRepository.findAll();
        List<MetaRegion> metaRegions = metaRegionRepository.findAllByOrderByIdAsc();
        List<String> regionCodes = regionId == null
                ? metaRegions.stream()
                    .map(MetaRegion::getName)
                    .map(this::legalDongRegionCode)
                    .filter(code -> code != null)
                    .distinct()
                    .toList()
                : parseMetaRegionId(regionId)
                    .flatMap(metaRegionRepository::findById)
                    .map(MetaRegion::getName)
                    .map(this::legalDongRegionCode)
                    .map(List::of)
                    .orElse(List.of());

        Map<Long, TourContentResponse> uniqueStays = new LinkedHashMap<>();
        regionCodes.forEach(code -> tourApiService.getStays(code, 100, 1)
                .forEach(stay -> uniqueStays.putIfAbsent(stay.getContentId(), stay)));

        return uniqueStays.values().stream()
                .map(stay -> toApiAccommodation(stay, regions, metaRegions))
                .filter(response -> regionId == null || regionId.equals(response.getRegionId()))
                .toList();
    }

    private java.util.Optional<Integer> parseMetaRegionId(String regionId) {
        try {
            return java.util.Optional.of(Integer.valueOf(regionId));
        } catch (NumberFormatException e) {
            return java.util.Optional.empty();
        }
    }

    private String legalDongRegionCode(String regionName) {
        if (regionName == null) return null;
        return LEGAL_DONG_REGION_CODES.entrySet().stream()
                .filter(entry -> regionName.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private AccommodationResponse toApiAccommodation(
            TourContentResponse stay,
            List<Region> regions,
            List<MetaRegion> metaRegions
    ) {
        String address = String.join(" ",
                stay.getAddr1() == null ? "" : stay.getAddr1(),
                stay.getAddr2() == null ? "" : stay.getAddr2()).trim();
        Region matchedRegion = regions.stream()
                .filter(region -> addressMatchesRegion(address, region.getName()))
                .findFirst()
                .orElse(null);
        MetaRegion matchedMetaRegion = metaRegions.stream()
                .filter(region -> (stay.getAreaCode() != null && stay.getAreaCode().equals(region.getId()))
                        || addressMatchesMetaRegion(address, region.getName()))
                .findFirst()
                .orElse(null);
        String district = matchedRegion == null
                ? (stay.getAddr2() == null ? "지역 미분류" : stay.getAddr2())
                : matchedRegion.getName();

        return AccommodationResponse.builder()
                .id(String.valueOf(stay.getContentId()))
                .name(stay.getTitle())
                .regionId(matchedMetaRegion == null ? null : String.valueOf(matchedMetaRegion.getId()))
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

    private boolean addressMatchesMetaRegion(String address, String regionName) {
        if (address == null || regionName == null) return false;
        String normalized = regionName
                .replace("특별자치시", "")
                .replace("특별자치도", "")
                .replace("특별시", "")
                .replace("광역시", "")
                .replace("도", "");
        return address.contains(normalized);
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
