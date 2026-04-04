package com.swarna.todo.basicAuth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurationBasicAuth {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {}) // ✅ enable CORS
                .csrf(csrf -> csrf.disable()) // disable CSRF

                .authorizeHttpRequests(auth -> auth
                        // ✅ allow preflight requests (important for Angular)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ all other requests require auth
                        .anyRequest().authenticated()
                )

                // ✅ Basic Auth
                .httpBasic(basic -> {})

                // ✅ stateless (recommended for APIs)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}