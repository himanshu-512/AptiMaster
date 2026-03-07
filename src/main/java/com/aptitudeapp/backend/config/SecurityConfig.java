package com.aptitudeapp.backend.config;

import com.aptitudeapp.backend.security.JwtAuthenticationFilter;
import com.aptitudeapp.backend.security.JwtTokenProvider;
import com.aptitudeapp.backend.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Allow CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/test").permitAll()

                        // Auth APIs
                        .requestMatchers("/api/auth/send-otp").permitAll()
                        .requestMatchers("/api/auth/verify-otp").permitAll()

                        // Protected APIs
                        .requestMatchers("/api/questions/**").authenticated()
                        .requestMatchers("/api/attempt/**").authenticated()
                        .requestMatchers("/api/analytics/**").authenticated()
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/ai/**").authenticated()
                        .requestMatchers("/api/leaderboard/**").authenticated()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}