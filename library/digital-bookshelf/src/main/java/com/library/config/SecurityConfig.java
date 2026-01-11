package com.library.config;

import org.springframework.beans.factory.annotation.Value; // Import Value
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {


    @Value("${app.security.librarian-password}")
    private String libPass;

    @Value("${app.security.member-password}")
    private String memPass;

    @Value("${app.security.admin-password}")
    private String adminPass;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/books").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails librarian = User.withDefaultPasswordEncoder()
                .username("lib")
                .password(libPass) // Injected
                .roles("LIBRARIAN")
                .build();

        UserDetails member = User.withDefaultPasswordEncoder()
                .username("mem")
                .password(memPass) // Injected
                .roles("MEMBER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password(adminPass) // Injected
                .roles("ADMIN", "LIBRARIAN")
                .build();

        return new InMemoryUserDetailsManager(librarian, member, admin);
    }
}