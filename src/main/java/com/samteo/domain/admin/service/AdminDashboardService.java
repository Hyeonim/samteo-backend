package com.samteo.domain.admin.service;

import com.samteo.domain.admin.dto.response.AdminDailyStatsResponse;
import com.samteo.domain.admin.dto.response.AdminStatsResponse;
import com.samteo.domain.community.repository.CommunityPostRepository;
import com.samteo.domain.myplanner.repository.PersonalPlannerRepository;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final PersonalPlannerRepository personalPlannerRepository;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;
    private final CommunityPostRepository communityPostRepository;

    @Transactional(readOnly = true)
    public AdminStatsResponse getStats(int requestedDays) {
        int days = normalizeDays(requestedDays);
        LocalDate startDate = LocalDate.now().minusDays(days - 1L);
        LocalDateTime from = startDate.atStartOfDay();

        Map<LocalDate, Long> users = toDailyMap(userRepository.countDailyCreatedSince(from));
        Map<LocalDate, Long> planners = toDailyMap(personalPlannerRepository.countDailyCreatedSince(from));
        Map<LocalDate, Long> posts = toDailyMap(communityPostRepository.countDailyCreatedSince(from));

        List<AdminDailyStatsResponse> dailyStats = startDate.datesUntil(LocalDate.now().plusDays(1))
                .map(date -> AdminDailyStatsResponse.builder()
                        .date(date.toString())
                        .users(users.getOrDefault(date, 0L))
                        .planners(planners.getOrDefault(date, 0L))
                        .communityPosts(posts.getOrDefault(date, 0L))
                        .build())
                .toList();

        return AdminStatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalPlanners(personalPlannerRepository.count())
                .totalJobs(jobRepository.count())
                .totalAccommodations(accommodationRepository.count())
                .totalCommunityPosts(communityPostRepository.countByDeletedAtIsNull())
                .periodDays(days)
                .dailyStats(dailyStats)
                .build();
    }

    private int normalizeDays(int days) {
        return days == 7 || days == 30 ? days : 14;
    }

    private Map<LocalDate, Long> toDailyMap(List<Object[]> rows) {
        Map<LocalDate, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            LocalDate date = toLocalDate(row[0]);
            long count = row[1] instanceof Number number ? number.longValue() : 0L;
            result.put(date, count);
        }
        return result;
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate date) return date;
        if (value instanceof Date date) return date.toLocalDate();
        return LocalDate.parse(String.valueOf(value));
    }
}
