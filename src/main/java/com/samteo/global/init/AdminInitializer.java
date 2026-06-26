package com.samteo.global.init;

import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@samteo.org").isPresent()) {
            return;
        }
        String hash = passwordEncoder.encode("samteo12");
        User admin = User.createAdmin("admin@samteo.org", "admin", hash);
        userRepository.save(admin);
        log.info("Admin account created: admin@samteo.org");
    }
}
