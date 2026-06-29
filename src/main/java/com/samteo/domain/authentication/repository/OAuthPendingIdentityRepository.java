package com.samteo.domain.authentication.repository;

import com.samteo.domain.authentication.entity.OAuthPendingIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthPendingIdentityRepository extends JpaRepository<OAuthPendingIdentity, Long> {

    Optional<OAuthPendingIdentity> findByPendingToken(String pendingToken);
}
