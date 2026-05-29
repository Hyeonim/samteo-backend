package com.samteo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

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

    public static User createLocal(String email, String name, String passwordHash) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.provider = "local";
        user.passwordHash = passwordHash;
        return user;
    }

    public static User createOAuth(String email, String name, String provider, String providerId) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.provider = provider;
        user.providerId = providerId;
        user.passwordHash = null;
        return user;
    }
}
