package com.samteo.domain.authentication.repository;

import com.samteo.domain.authentication.entity.UserAuthIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAuthIdentityRepository extends JpaRepository<UserAuthIdentity, Long> {

    Optional<UserAuthIdentity> findByProviderAndProviderUserId(String provider, String providerUserId);

    Optional<UserAuthIdentity> findByUserIdAndProvider(Long userId, String provider);

    List<UserAuthIdentity> findAllByUserIdOrderByLinkedAtAsc(Long userId);
}
