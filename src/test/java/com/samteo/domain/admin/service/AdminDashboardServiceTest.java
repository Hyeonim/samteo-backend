package com.samteo.domain.admin.service;

import com.samteo.domain.admin.dto.response.AdminStatsResponse;
import com.samteo.domain.community.repository.CommunityPostRepository;
import com.samteo.domain.myplanner.repository.PersonalPlannerRepository;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import com.samteo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminDashboardServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PersonalPlannerRepository personalPlannerRepository;
    @Mock private JobRepository jobRepository;
    @Mock private AccommodationRepository accommodationRepository;
    @Mock private CommunityPostRepository communityPostRepository;

    @InjectMocks
    private AdminDashboardService adminDashboardService;

    @Test
    void fillsMissingDatesAndReturnsPeriodMetrics() {
        LocalDate today = LocalDate.now();
        when(userRepository.count()).thenReturn(12L);
        when(personalPlannerRepository.count()).thenReturn(7L);
        when(jobRepository.count()).thenReturn(30L);
        when(accommodationRepository.count()).thenReturn(40L);
        when(communityPostRepository.countByDeletedAtIsNull()).thenReturn(9L);
        when(userRepository.countDailyCreatedSince(any(LocalDateTime.class)))
                .thenReturn(List.<Object[]>of(new Object[]{Date.valueOf(today), 2L}));
        when(personalPlannerRepository.countDailyCreatedSince(any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(communityPostRepository.countDailyCreatedSince(any(LocalDateTime.class)))
                .thenReturn(List.<Object[]>of(new Object[]{Date.valueOf(today.minusDays(1)), 3L}));

        AdminStatsResponse response = adminDashboardService.getStats(7);

        assertThat(response.getPeriodDays()).isEqualTo(7);
        assertThat(response.getDailyStats()).hasSize(7);
        assertThat(response.getDailyStats().get(5).getCommunityPosts()).isEqualTo(3L);
        assertThat(response.getDailyStats().get(6).getUsers()).isEqualTo(2L);
        assertThat(response.getDailyStats()).allSatisfy(metric -> assertThat(metric.getPlanners()).isZero());
        assertThat(response.getTotalCommunityPosts()).isEqualTo(9L);
    }
}
