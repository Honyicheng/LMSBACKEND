package com.library.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.auth.security.JwtUtils;
import com.library.common.entity.User;

import com.library.common.repository.UserRepository;
import com.library.common.repository.RoleRepository;
import com.library.config.AppProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Slf4j

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AppProperties appProperties;

    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//            throws IOException, ServletException {
//
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oAuth2User.getAttribute("email");
//        String username = oAuth2User.getAttribute("login");
//
//        User user = userRepository.findByEmail(email).orElseGet(() -> {
//            User newUser = new User();
//            newUser.setUsername(username);
//            newUser.setEmail(email);
//            newUser.setPassword("OAUTH2"); // dummy password
//            newUser.setRoles(Collections.singleton(
//                roleRepository.findByName(ERole.ROLE_MEMBER).orElseThrow()
//            ));
//            return userRepository.save(newUser);
//        });
//
//        String token = jwtUtils.generateJwtToken(authentication);
//        response.sendRedirect("/api/auth/oauth2/success?token=" + token);
//    }
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String username = oauthUser.getAttribute("login");      // GitHub用户名
        String email = oauthUser.getAttribute("email");         // 可能为 null
        if (username == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "GitHub username not found");
            return;
        }
        log.info("GitHub username: {}", username);;
        Optional<User> existingUser = userRepository.findByUsername(username);
        User user = existingUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setMembershipDate(LocalDateTime.now());
            newUser.setEmail(email != null ? email : username + "@github.com");
            newUser.setPassword(UUID.randomUUID().toString()); // 随机密码占位
            newUser.setRoles(Collections.singleton(
                    roleRepository.findByName(com.library.entity.ERole.ROLE_MEMBER).orElseThrow()
            ));
            return userRepository.save(newUser);
        });

        String token = jwtUtils.generateJwtTokenFromUsername(user.getUsername());
        log.info("Generated JWT token: {}", token);
        String redirectUri = "http://localhost:5173/oauth2/redirect?token=" + token;
//        String redirectUri = appProperties.getRedirectUri();
        //response.sendRedirect(redirectUri + "?token=" + token);
        response.sendRedirect(redirectUri);
    }

}