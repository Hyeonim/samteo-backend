package com.samteo.domain.admin.controller;

import com.samteo.domain.admin.dto.request.RoleUpdateRequest;
import com.samteo.domain.admin.dto.response.AdminAccommodationResponse;
import com.samteo.domain.admin.dto.response.AdminCommunityPostResponse;
import com.samteo.domain.admin.dto.response.AdminJobResponse;
import com.samteo.domain.admin.dto.response.AdminPlannerResponse;
import com.samteo.domain.admin.dto.response.AdminStatsResponse;
import com.samteo.domain.admin.dto.response.AdminUserResponse;
import com.samteo.domain.admin.dto.response.ApiStatusResponse;
import com.samteo.domain.admin.service.AdminApiStatusService;
import com.samteo.domain.admin.service.AdminCommunityService;
import com.samteo.domain.community.dto.CommunityPostUpdateRequest;
import com.samteo.domain.myplanner.entity.PersonalPlanner;
import com.samteo.domain.myplanner.repository.PersonalPlannerRepository;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final PersonalPlannerRepository personalPlannerRepository;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;
    private final AdminApiStatusService adminApiStatusService;
    private final AdminCommunityService adminCommunityService;

    // ── 대시보드 ──────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getStats() {
        AdminStatsResponse stats = AdminStatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalPlanners(personalPlannerRepository.count())
                .totalJobs(jobRepository.count())
                .totalAccommodations(accommodationRepository.count())
                .build();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    // ── 회원 관리 ─────────────────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<AdminUserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminUserResponse> users = userRepository
                .findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(AdminUserResponse::from);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<AdminUserResponse>> updateRole(
            @PathVariable Long id,
            @RequestBody RoleUpdateRequest request,
            @AuthenticationPrincipal Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new IllegalArgumentException("자신의 권한은 변경할 수 없습니다.");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.updateRole(request.getRole());
        User saved = userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success(AdminUserResponse.from(saved)));
    }

    // ── 공고 관리 ─────────────────────────────────────────────────────────────

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<Page<AdminJobResponse>>> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String keyword) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<AdminJobResponse> result = StringUtils.hasText(keyword)
                ? jobRepository.searchByKeyword(keyword, pageable).map(AdminJobResponse::from)
                : jobRepository.findAll(pageable).map(AdminJobResponse::from);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/jobs/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable String id) {
        if (!jobRepository.existsById(id)) throw new IllegalArgumentException("Job not found: " + id);
        jobRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── 숙소 관리 (미사용 — TourAPI 실시간 조회) ──────────────────────────────

    @GetMapping("/accommodations")
    public ResponseEntity<ApiResponse<Page<AdminAccommodationResponse>>> getAccommodations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String keyword) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<AdminAccommodationResponse> result = StringUtils.hasText(keyword)
                ? accommodationRepository.searchByKeyword(keyword, pageable).map(AdminAccommodationResponse::from)
                : accommodationRepository.findAll(pageable).map(AdminAccommodationResponse::from);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/accommodations/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteAccommodation(@PathVariable String id) {
        if (!accommodationRepository.existsById(id)) throw new IllegalArgumentException("Accommodation not found: " + id);
        accommodationRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── 플래너 현황 ───────────────────────────────────────────────────────────

    @GetMapping("/planners")
    public ResponseEntity<ApiResponse<Page<AdminPlannerResponse>>> getPlanners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<PersonalPlanner> planners = personalPlannerRepository
                .findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));

        Set<Long> userIds = planners.getContent().stream()
                .map(PersonalPlanner::getUserId)
                .collect(Collectors.toSet());
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getUserId, u -> u));

        Page<AdminPlannerResponse> result = planners.map(p -> AdminPlannerResponse.from(p, userMap.get(p.getUserId())));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/community/posts")
    public ResponseEntity<ApiResponse<Page<AdminCommunityPostResponse>>> getCommunityPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(ApiResponse.success(adminCommunityService.getPosts(page, size, keyword)));
    }

    @PutMapping("/community/posts/{postId}")
    public ResponseEntity<ApiResponse<AdminCommunityPostResponse>> updateCommunityPost(
            @PathVariable Long postId,
            @RequestBody CommunityPostUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                adminCommunityService.updatePost(postId, request.getContent())
        ));
    }

    @DeleteMapping("/community/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteCommunityPost(@PathVariable Long postId) {
        adminCommunityService.deletePost(postId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ── API 상태 ──────────────────────────────────────────────────────────────

    @GetMapping("/api-status")
    public ResponseEntity<ApiResponse<List<ApiStatusResponse>>> getApiStatus() {
        return ResponseEntity.ok(ApiResponse.success(adminApiStatusService.checkAll()));
    }
}
