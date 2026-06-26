package com.samteo.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

/**
 * 공용 {@code users} 테이블에 저장되는 애플리케이션 사용자 엔티티이다.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role", nullable = false)
    private String role = "USER";

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
     * 해시된 비밀번호를 포함하는 로컬 계정 엔티티를 생성한다.
     *
     * @param email 계정 이메일
     * @param name 표시 이름
     * @param passwordHash 해시된 비밀번호
     * @return 로컬 사용자 엔티티
     */
    public static User createLocal(String email, String name, String passwordHash) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.provider = "local";
        user.passwordHash = passwordHash;
        user.role = "USER";
        return user;
    }

    public void updateRole(String role) {
        this.role = role;
    }

    public static User createAdmin(String email, String name, String passwordHash) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.provider = "local";
        user.passwordHash = passwordHash;
        user.role = "ADMIN";
        return user;
    }

    /**
     * OAuth 기반 사용자 계정 엔티티를 생성한다.
     *
     * @param email 계정 이메일
     * @param name 표시 이름
     * @param provider OAuth 제공자 이름
     * @param providerId 제공자가 발급한 사용자 고유 식별자
     * @return OAuth 사용자 엔티티
     */
    public static User createOAuth(String email, String name, String provider, String providerId) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.provider = provider;
        user.providerId = providerId;
        return user;
    }
}
