package com.jithin.nudge.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jithin.nudge.handler.UserAuthenticationFailureHandler;
import com.jithin.nudge.handler.UserAuthenticationSuccessHandler;
import com.jithin.nudge.security.HttpBasicDecodingFilter;
import com.jithin.nudge.security.JWTAuthFilter;
import com.jithin.nudge.security.UserAuthenticationFilter;
import com.jithin.nudge.security.UserAuthenticationManager;
import com.jithin.nudge.security.UserAuthenticationProvider;
import com.jithin.nudge.service.JWTService;
import com.jithin.nudge.service.UserService;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

        private final UserService userService;
        private final JWTService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final SecurityProperties securityProperties;

        public SecurityConfig(UserService userService, JWTService jwtService, PasswordEncoder passwordEncoder, SecurityProperties securityProperties) {
                this.userService = userService;
                this.jwtService = jwtService;
                this.passwordEncoder = passwordEncoder;
                this.securityProperties = securityProperties;
        }

        @Bean
        public String loginProcessingUrl() {
                return "/api/v1/auth/login";
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                if (securityProperties.allowedOrigins() != null) {
                        configuration.setAllowedOrigins(Arrays.asList(securityProperties.allowedOrigins().split(",")));
                }
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        public Filter jwtAuthFilter() {
                return new JWTAuthFilter(jwtService, userService, userAuthenticationFailureHandler());
        }

        public Filter httpBasicFilter() {
                return new HttpBasicDecodingFilter();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        public UserAuthenticationProvider userAuthenticationProvider() {
                return new UserAuthenticationProvider(userService, passwordEncoder);
        }

        @Bean
        public UserAuthenticationManager userAuthenticationManager() {
                return new UserAuthenticationManager(userAuthenticationProvider());
        }

        public UserAuthenticationFailureHandler userAuthenticationFailureHandler() {
                return new UserAuthenticationFailureHandler();
        }

        public UserAuthenticationSuccessHandler userAuthenticationSuccessHandler() {
                return new UserAuthenticationSuccessHandler(jwtService);
        }

        public UserAuthenticationFilter userAuthFilter(UserAuthenticationManager userAuthenticationManager) {
                UserAuthenticationFilter filter = new UserAuthenticationFilter(loginProcessingUrl(), userAuthenticationManager);
                filter.setAuthenticationFailureHandler(userAuthenticationFailureHandler());
                filter.setAuthenticationSuccessHandler(userAuthenticationSuccessHandler());
                return filter;
        }

        @Bean
        @Order(1)
        public SecurityFilterChain securityFilterChainLogin(HttpSecurity http, UserAuthenticationManager userAuthenticationManager) throws Exception {
                http.securityMatcher("/api/v1/auth/login/**").cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()).csrf(csrf -> csrf.disable())
                                .addFilterBefore(httpBasicFilter(), UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(userAuthFilter(userAuthenticationManager), UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain securityFilterChainPublic(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/*/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/*/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll())
                                .csrf(csrf -> csrf.disable());
                return http.build();
        }

        @Bean
        @Order(3)
        public SecurityFilterChain securityFilterChainProtected(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/**").cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()).addFilterAfter(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}
