package com.samteo.domain.myplanner.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자가 소유하는 개인 플래너 루트 엔티티이다.
 */
@Entity
@Table(name = "personal_planners")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalPlanner {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlannerEventType> eventTypes = new ArrayList<>();

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlannerSchedule> schedules = new ArrayList<>();

    @Column(name = "region_name", length = 200)
    private String regionName;

    @Column(name = "accommodation_cost")
    private Long accommodationCost;

    @Column(name = "total_salary")
    private Long totalSalary;

    @Column(name = "disposable_income")
    private Long disposableIncome;

    @Column(name = "fixed_expense")
    private Long fixedExpense;

    @Column(name = "accommodation_json", columnDefinition = "TEXT")
    private String accommodationJson;

    @Column(name = "jobs_json", columnDefinition = "TEXT")
    private String jobsJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    /**
     * 새로운 개인 플래너 인스턴스를 생성한다.
     *
     * @param id     클라이언트에서 생성한 UUID
     * @param userId 소유자 사용자 ID
     * @param title  플래너 제목
     * @param memo   메모 (nullable)
     * @return 생성된 플래너 엔티티
     */
    public static PersonalPlanner create(String id, Long userId, String title, String memo) {
        PersonalPlanner planner = new PersonalPlanner();
        planner.id = id;
        planner.userId = userId;
        planner.title = title;
        planner.memo = memo;
        return planner;
    }

    /**
     * 플래너의 제목과 메모를 수정한다.
     *
     * @param title 새 제목
     * @param memo  새 메모 (nullable)
     */
    public void update(String title, String memo) {
        this.title = title;
        this.memo = memo;
    }

    /**
     * 플래너의 재정 메타데이터를 업데이트한다.
     */
    public void updateFinancials(String regionName, Long accommodationCost, Long totalSalary,
                                  Long disposableIncome, Long fixedExpense,
                                  String accommodationJson, String jobsJson) {
        this.regionName = regionName;
        this.accommodationCost = accommodationCost;
        this.totalSalary = totalSalary;
        this.disposableIncome = disposableIncome;
        this.fixedExpense = fixedExpense;
        this.accommodationJson = accommodationJson;
        this.jobsJson = jobsJson;
    }

    /**
     * 이벤트 타입 목록 전체를 교체한다.
     *
     * @param newTypes 교체할 이벤트 타입 목록
     */
    public void replaceEventTypes(List<PlannerEventType> newTypes) {
        this.eventTypes.clear();
        this.eventTypes.addAll(newTypes);
    }

    /**
     * 스케줄 목록 전체를 교체한다.
     *
     * @param newSchedules 교체할 스케줄 목록
     */
    public void replaceSchedules(List<PlannerSchedule> newSchedules) {
        this.schedules.clear();
        this.schedules.addAll(newSchedules);
    }
}
