package com.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.starter.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsSource) throws Exception {

    	http
	    	.cors(cors -> cors.configurationSource(corsSource)) // Enable CORS using global CorsConfiguration
	        .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs (common for JWT)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/swagger/**", "/v3/api-docs/**", "/swagger-ui/**", "/actuator/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
