package com.l1mit.qma_server.global.config;

import static org.springframework.http.HttpMethod.POST;

import com.l1mit.qma_server.global.filter.JwtAuthenticationEntryPoint;
import com.l1mit.qma_server.global.filter.JwtAuthenticationFilter;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(
                        AbstractHttpConfigurer::disable)
                .csrf(
                        AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        setAuthorizeHttpRequests())
                .headers(
                        headers -> headers.frameOptions(FrameOptionsConfig::disable))
                .sessionManagement(
                        config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> setAuthorizeHttpRequests() {
        return requests ->
                requests.requestMatchers(Stream.of(PERMIT_PATTERNS)
                                .map(AntPathRequestMatcher::antMatcher)
                                .toArray(AntPathRequestMatcher[]::new))
                        .permitAll()

                        .requestMatchers(Stream.of(HAS_ANY_ROLE_PATTERNS)
                                .map(AntPathRequestMatcher::antMatcher)
                                .toArray(AntPathRequestMatcher[]::new))
                        .hasAnyRole("MEMBER", "ADMIN")

                        .requestMatchers(new AntPathRequestMatcher("/api/v1/question", POST.name()))
                        .hasAnyRole("MEMBER", "ADMIN")

                        .requestMatchers(new AntPathRequestMatcher("/api/v1/answer", POST.name()))
                        .hasAnyRole("MEMBER", "ADMIN")

                        .requestMatchers(new AntPathRequestMatcher("/**"))
                        .authenticated();
    }

    private static final String[] PERMIT_PATTERNS = new String[]{
            "/api/v1/**",
            "/h2-console/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/docs/**"
    };

    private static final String[] HAS_ANY_ROLE_PATTERNS = new String[]{
            "/api/v1/auth/my-info",
    };
}
