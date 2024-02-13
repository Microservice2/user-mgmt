package com.user.usermgmt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String ADMIN = "admin";
    public static final String USER = "user";
    private final JwtConverter jwtConverter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) ->
                authz.requestMatchers(HttpMethod.GET, "/say/hello").permitAll()
                        .requestMatchers(HttpMethod.GET, "/say/admin/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/say/user/**").hasRole(USER)
                        .requestMatchers(HttpMethod.GET, "/say/admin-and-user/**").hasAnyRole(ADMIN,USER)
                        .requestMatchers(HttpMethod.POST, "/api/user/createNew/**").hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/user/deleteEmployeeById/**").hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/user/updateEmployee/**").hasAnyRole(ADMIN, USER)
                        .requestMatchers(HttpMethod.GET, "/api/user/getEmployees?**").hasAnyRole(ADMIN, USER)
                        .requestMatchers(HttpMethod.GET, "/api/user/getDetailEmployeeById/**").hasAnyRole(ADMIN, USER)
                        .anyRequest().authenticated());

        http.sessionManagement(sess -> sess.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

        return http.build();
    }
}
