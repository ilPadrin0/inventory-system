package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backend.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;  // renamed
    private final DaoAuthenticationProvider authenticationProvider;
    
    public SecurityConfig(JwtUtil jwtUtil,
	            UserRepository userRepository,
	            DaoAuthenticationProvider authenticationProvider) {
	this.jwtUtil = jwtUtil;
	this.userRepository = userRepository;
	this.authenticationProvider = authenticationProvider;
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // use the renamed field here
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, userRepository);

        http
          .csrf().disable()
          .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/api/auth/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/products/**")
                .hasAnyRole("USER","ADMIN")
              .requestMatchers("/api/products/**")
                .hasRole("ADMIN")
              .anyRequest().authenticated()
          )
          .authenticationProvider(authenticationProvider)
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
      AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
