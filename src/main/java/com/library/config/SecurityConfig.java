package com.library.config;

import com.library.auth.oauth2.OAuth2FailureHandler;
import com.library.auth.oauth2.OAuth2SuccessHandler;
import com.library.auth.security.AuthTokenFilter;
import com.library.auth.security.JwtAuthEntryPoint;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired private AuthTokenFilter jwtAuthFilter;
    @Autowired private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired private OAuth2FailureHandler oAuth2FailureHandler;
    @Autowired private JwtAuthEntryPoint jwtAuthEntryPoint;



//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 新方式启用 CORS
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**", "/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/public/books/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasRole("MEMBER")
//                        .requestMatchers("/api/public/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .successHandler(oAuth2SuccessHandler)
//                        .failureHandler(oAuth2FailureHandler)
//                );
//
//        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/books/**").hasRole("ADMIN")
                        .requestMatchers("/api/reviews/**").hasRole("MEMBER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))
                .oauth2Login(oauth2 -> oauth2
                        // entry point for authorization requests
                        .authorizationEndpoint(a -> a.baseUri("/oauth2/authorize"))
                        // GitHub will call back here
//                        .redirectionEndpoint(r -> r.baseUri("/oauth2/callback/*"))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}