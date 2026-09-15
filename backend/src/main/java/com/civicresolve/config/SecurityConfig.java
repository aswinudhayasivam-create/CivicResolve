package com.civicresolve.config;

import com.civicresolve.security.JwtFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.*;

@Configuration
public class SecurityConfig {

    @Value("${app.cors.origin:http://localhost:5173}")
    private String corsOrigin;

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filter(HttpSecurity h, JwtFilter jf) throws Exception {

        return h
            .csrf(c -> c.disable())

            .cors(c -> c.configurationSource(request -> {

                var config = new CorsConfiguration();

                config.setAllowedOrigins(
                    Arrays.stream(corsOrigin.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
                );

                config.setAllowedMethods(
                    List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                );

                config.setAllowedHeaders(List.of("*"));

                config.setAllowCredentials(false);

                return config;
            }))

            .sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(a ->
                a.requestMatchers(
                    "/api/auth/**",
                    "/api/categories",
                    "/api/complaints/track/**"
                ).permitAll()

                .requestMatchers("/api/admin/**")
                .hasAnyRole("ADMIN", "AUTHORITY")

                .anyRequest()
                .authenticated()
            )

            .addFilterBefore(
                jf,
                UsernamePasswordAuthenticationFilter.class
            )

            .build();
    }
}