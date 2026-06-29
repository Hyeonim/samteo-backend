package com.samteo.domain.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "user_auth_identities",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_auth_identity_provider_user", columnNames = {"provider", "provider_user_id"}),
                @UniqueConstraint(name = "uk_auth_identity_user_provider", columnNames = {"user_id", "provider"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identity_id")
    private Long identityId;

    // Logical reference to users.user_id. The database intentionally has no FK constraint.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    @Column(name = "provider_email")
    private String providerEmail;

    @Column(name = "linked_at", nullable = false, updatable = false)
    private OffsetDateTime linkedAt;

    @Column(name = "last_login_at", nullable = false)
    private OffsetDateTime lastLoginAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        linkedAt = now;
        lastLoginAt = now;
    }

    public static UserAuthIdentity create(
            Long userId,
            String provider,
            String providerUserId,
            String providerEmail
    ) {
        UserAuthIdentity identity = new UserAuthIdentity();
        identity.userId = userId;
        identity.provider = provider;
        identity.providerUserId = providerUserId;
        identity.providerEmail = providerEmail;
        return identity;
    }

    public void recordLogin(String providerEmail) {
        this.providerEmail = providerEmail;
        this.lastLoginAt = OffsetDateTime.now();
    }
}
