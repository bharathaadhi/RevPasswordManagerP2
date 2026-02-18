package com.rev.revpasswordmanagerp2.config;

import com.rev.revpasswordmanagerp2.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // ✅ PASSWORD ENCODER BEAN (ONLY ONE IN PROJECT)
    @Bean(name = "securityPasswordEncoder")
    public PasswordEncoder securityPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // ✅ SECURITY FILTER CHAIN
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 🔥 Disable CSRF for REST API
                .csrf(csrf -> csrf.disable())

                // 🔥 Authorize endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/auth/forgot-password"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 🔥 Add JWT Filter BEFORE UsernamePassword filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 🔥 Disable default login page
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
