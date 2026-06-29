package com.samteo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.filter.JwtAuthFilter;
import com.samteo.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(), ApiResponse.error("로그인이 만료되었거나 필요합니다."));
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/login/oauth2/**", "/api/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/community/posts/me").authenticated()
                        .requestMatchers(HttpMethod.GET,
                                "/api/users/*/profile",
                                "/api/planner/bootstrap",
                                "/api/planner/jobs",
                                "/api/planner/jobs/page",
                                "/api/planner/accommodations",
                                "/api/planner/map-provider",
                                "/api/planner/load-lane",
                                "/api/regions",
                                "/api/festivals",
                                "/api/attractions",
                                "/api/restaurants",
                                "/api/tour/spots",
                                "/api/tour/festivals",
                                "/api/tour/detail/common",
                                "/api/tour/detail/intro",
                                "/api/community/posts",
                                "/api/community/posts/users/*",
                                "/api/community/posts/tags/*",
                                "/api/community/posts/*",
                                "/api/community/posts/*/comments",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/planner/transit-routes",
                                "/api/planner/budget"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
