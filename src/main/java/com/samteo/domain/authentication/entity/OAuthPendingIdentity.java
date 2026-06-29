package com.samteo.domain.authentication.entity;

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

import java.time.OffsetDateTime;

@Entity
@Table(name = "oauth_pending_identities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthPendingIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pending_id")
    private Long pendingId;

    @Column(name = "pending_token", nullable = false, unique = true, length = 100)
    private String pendingToken;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    @Column(name = "provider_email", nullable = false)
    private String providerEmail;

    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "consumed_at")
    private OffsetDateTime consumedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    public static OAuthPendingIdentity create(
            String pendingToken,
            String provider,
            String providerUserId,
            String providerEmail,
            String providerName,
            OffsetDateTime expiresAt
    ) {
        OAuthPendingIdentity pending = new OAuthPendingIdentity();
        pending.pendingToken = pendingToken;
        pending.provider = provider;
        pending.providerUserId = providerUserId;
        pending.providerEmail = providerEmail;
        pending.providerName = providerName;
        pending.expiresAt = expiresAt;
        return pending;
    }

    public boolean isAvailable(OffsetDateTime now) {
        return consumedAt == null && expiresAt.isAfter(now);
    }

    public void consume() {
        consumedAt = OffsetDateTime.now();
    }
}
