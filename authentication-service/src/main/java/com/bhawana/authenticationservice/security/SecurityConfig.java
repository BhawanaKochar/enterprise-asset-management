package com.bhawana.authenticationservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/register",
                                "/error",
                                "/actuator/**",
                                "/saml2/**",
                                "/login/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .saml2Login(saml -> saml
                        .defaultSuccessUrl("http://localhost:5173/dashboard", true)
                )

                .saml2Logout(Customizer.withDefaults())

                .saml2Metadata(Customizer.withDefaults());

        return http.build();
    }
}