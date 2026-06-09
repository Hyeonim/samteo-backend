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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

    @Transactional(readOnly = true)
    public PlannerBootstrapResponse getBootstrapData() {
        return PlannerBootstrapResponse.builder()
                .regions(regionRepository.findAll().stream().map(RegionResponse::from).toList())
                .jobs(getJobs(null))
                .accommodations(getAccommodations(null))
                .mapProvider(getMapProvider())
                .build();
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobs(String regionId) {
        List<Job> jobs = regionId == null ? jobRepository.findAll() : jobRepository.findByRegionId(regionId);
        return jobs.stream()
                .map(JobResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAccommodations(String regionId) {
        List<Accommodation> accommodations = regionId == null
                ? accommodationRepository.findAllByOrderByCommuteMinutesAsc()
                : accommodationRepository.findByRegionIdOrderByCommuteMinutesAsc(regionId);
        return accommodations.stream()
                .map(AccommodationResponse::from)
                .toList();
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
