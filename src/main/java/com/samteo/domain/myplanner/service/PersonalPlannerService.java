package com.samteo.domain.myplanner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.myplanner.dto.EventTypeDto;
import com.samteo.domain.myplanner.dto.PersonalPlannerRequest;
import com.samteo.domain.myplanner.dto.PersonalPlannerResponse;
import com.samteo.domain.myplanner.dto.ScheduleDto;
import com.samteo.domain.myplanner.entity.PersonalPlanner;
import com.samteo.domain.myplanner.entity.PlannerDefaultEventType;
import com.samteo.domain.myplanner.entity.PlannerEventType;
import com.samteo.domain.myplanner.entity.PlannerSchedule;
import com.samteo.domain.myplanner.repository.PersonalPlannerRepository;
import com.samteo.domain.myplanner.repository.PlannerDefaultEventTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 개인 플래너의 CRUD 비즈니스 로직을 담당하는 서비스이다.
 */
@Service
@RequiredArgsConstructor
public class PersonalPlannerService {

    private final PersonalPlannerRepository personalPlannerRepository;
    private final PlannerDefaultEventTypeRepository plannerDefaultEventTypeRepository;
    private final ObjectMapper objectMapper;

    /**
     * 현재 사용자가 소유한 플래너 목록 전체를 조회한다.
     *
     * @param userId 인증된 사용자 ID
     * @return 플래너 응답 목록
     */
    @Transactional(readOnly = true)
    public List<PersonalPlannerResponse> getAll(Long userId) {
        return personalPlannerRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 개인 플래너를 생성한다.
     *
     * @param userId 인증된 사용자 ID
     * @param req    생성 요청 DTO (id 포함)
     * @return 생성된 플래너 응답
     */
    @Transactional
    public PersonalPlannerResponse create(Long userId, PersonalPlannerRequest req) {
        PersonalPlanner planner = PersonalPlanner.create(normalizeId(req.getId()), userId, req.getTitle(), req.getMemo());
        setEventTypes(planner, eventTypesOrDefaults(req.getEventTypes()));
        setSchedules(planner, req.getSchedule());
        planner.updateFinancials(
                req.getRegionName(),
                req.getAccommodationCost(),
                req.getTotalSalary(),
                req.getDisposableIncome(),
                req.getFixedExpense(),
                toJson(req.getAccommodation()),
                toJson(req.getJobs())
        );
        return toResponse(personalPlannerRepository.save(planner));
    }

    /**
     * 기존 플래너의 내용을 수정한다. 소유권이 없으면 예외를 던진다.
     *
     * @param userId     인증된 사용자 ID
     * @param plannerId  수정할 플래너 UUID
     * @param req        수정 요청 DTO
     * @return 수정된 플래너 응답
     */
    @Transactional
    public PersonalPlannerResponse update(Long userId, String plannerId, PersonalPlannerRequest req) {
        PersonalPlanner planner = findOwnedPlanner(userId, plannerId);
        planner.update(req.getTitle(), req.getMemo());
        setEventTypes(planner, req.getEventTypes());
        setSchedules(planner, req.getSchedule());
        planner.updateFinancials(
                req.getRegionName(),
                req.getAccommodationCost(),
                req.getTotalSalary(),
                req.getDisposableIncome(),
                req.getFixedExpense(),
                toJson(req.getAccommodation()),
                toJson(req.getJobs())
        );
        return toResponse(planner);
    }

    /**
     * 플래너를 삭제한다. 소유권이 없으면 예외를 던진다.
     *
     * @param userId    인증된 사용자 ID
     * @param plannerId 삭제할 플래너 UUID
     */
    @Transactional
    public void delete(Long userId, String plannerId) {
        PersonalPlanner planner = findOwnedPlanner(userId, plannerId);
        personalPlannerRepository.delete(planner);
    }

    // --- private helpers ---

    private PersonalPlanner findOwnedPlanner(Long userId, String plannerId) {
        return personalPlannerRepository.findByIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("플래너를 찾을 수 없습니다."));
    }

    private void setEventTypes(PersonalPlanner planner, List<EventTypeDto> dtos) {
        if (dtos == null) {
            planner.replaceEventTypes(List.of());
            return;
        }
        List<PlannerEventType> entities = IntStream.range(0, dtos.size())
                .mapToObj(i -> {
                    EventTypeDto dto = dtos.get(i);
                    return PlannerEventType.of(planner, dto.getValue(), dto.getLabel(), dto.getColor(), i);
                })
                .collect(Collectors.toList());
        planner.replaceEventTypes(entities);
    }

    private List<EventTypeDto> eventTypesOrDefaults(List<EventTypeDto> dtos) {
        if (dtos != null && !dtos.isEmpty()) {
            return dtos;
        }
        return defaultEventTypes();
    }

    private List<EventTypeDto> defaultEventTypes() {
        return plannerDefaultEventTypeRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(this::toEventTypeDto)
                .collect(Collectors.toList());
    }

    private EventTypeDto toEventTypeDto(PlannerDefaultEventType type) {
        return new EventTypeDto(type.getValue(), type.getLabel(), type.getColor());
    }

    private void setSchedules(PersonalPlanner planner, List<ScheduleDto> dtos) {
        if (dtos == null) {
            planner.getSchedules().clear();
            return;
        }

        Map<String, PlannerSchedule> existingById = planner.getSchedules().stream()
                .collect(Collectors.toMap(PlannerSchedule::getId, s -> s));

        Set<String> incomingIds = new HashSet<>();
        List<PlannerSchedule> toAdd = new ArrayList<>();

        for (ScheduleDto dto : dtos) {
            String id = normalizeId(dto.getId());
            incomingIds.add(id);
            PlannerSchedule existing = existingById.get(id);
            if (existing != null) {
                existing.update(dto.getTitle(), dto.getDay(), dto.getStart(), dto.getEnd(),
                        dto.getType(), dto.getTypeLabel(), dto.getColor(), dto.getMemo(),
                        dto.getDateKey(), dto.getRepeatMode(), dto.isLocked());
            } else {
                toAdd.add(PlannerSchedule.of(planner, id, dto.getTitle(), dto.getDay(),
                        dto.getStart(), dto.getEnd(), dto.getType(), dto.getTypeLabel(),
                        dto.getColor(), dto.getMemo(), dto.getDateKey(), dto.getRepeatMode(),
                        dto.isLocked()));
            }
        }

        planner.getSchedules().removeIf(s -> !incomingIds.contains(s.getId()));
        planner.getSchedules().addAll(toAdd);
    }

    private PersonalPlannerResponse toResponse(PersonalPlanner planner) {
        List<EventTypeDto> eventTypeDtos = planner.getEventTypes().stream()
                .map(et -> new EventTypeDto(et.getValue(), et.getLabel(), et.getColor()))
                .collect(Collectors.toList());
        if (eventTypeDtos.isEmpty()) {
            eventTypeDtos = defaultEventTypes();
        }

        List<ScheduleDto> scheduleDtos = planner.getSchedules().stream()
                .map(s -> new ScheduleDto(
                        s.getId(),
                        s.getTitle(),
                        s.getDay(),
                        s.getStartTime(),
                        s.getEndTime(),
                        s.getTypeValue(),
                        s.getTypeLabel(),
                        s.getColor(),
                        s.getMemo(),
                        s.getDateKey(),
                        s.getRepeatMode(),
                        s.isLocked()
                ))
                .collect(Collectors.toList());

        return PersonalPlannerResponse.builder()
                .id(planner.getId())
                .title(planner.getTitle())
                .memo(planner.getMemo())
                .updatedAt(planner.getUpdatedAt().toString())
                .eventTypes(eventTypeDtos)
                .schedule(scheduleDtos)
                .regionName(planner.getRegionName())
                .accommodationCost(planner.getAccommodationCost())
                .totalSalary(planner.getTotalSalary())
                .disposableIncome(planner.getDisposableIncome())
                .fixedExpense(planner.getFixedExpense())
                .accommodation(fromJson(planner.getAccommodationJson(), new TypeReference<Map<String, Object>>() {}))
                .jobs(fromJson(planner.getJobsJson(), new TypeReference<List<Map<String, Object>>>() {}))
                .build();
    }

    private <T> String toJson(T value) {
        if (value == null) return null;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        if (json == null || json.isBlank()) return null;
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    private String normalizeId(String id) {
        if (id != null && !id.isBlank() && id.length() <= 36) {
            return id;
        }
        return UUID.randomUUID().toString();
    }
}
