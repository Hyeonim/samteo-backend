package com.samteo.domain.user.repository;

import com.samteo.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class DummyUserRepository {

    private final AtomicLong sequence = new AtomicLong(1);
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public DummyUserRepository() {
        User defaultUser = User.builder()
                .id(sequence.getAndIncrement())
                .email("demo@samteo.local")
                .nickname("Samteo Demo")
                .provider("LOCAL")
                .providerId("demo")
                .profileImageUrl(null)
                .role("USER")
                .build();
        users.put(defaultUser.getId(), defaultUser);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public User save(User user) {
        Long id = user.getId() == null ? sequence.getAndIncrement() : user.getId();
        User saved = User.builder()
                .id(id)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .build();
        users.put(id, saved);
        return saved;
    }
}
