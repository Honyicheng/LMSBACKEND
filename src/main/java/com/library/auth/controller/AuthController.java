package com.library.auth.controller;

import com.library.auth.payload.LoginRequest;
import com.library.auth.payload.SignupRequest;
import com.library.auth.security.JwtUtils;
import com.library.common.entity.Role;
import com.library.common.entity.User;

import com.library.common.repository.RoleRepository;
import com.library.common.repository.UserRepository;
import com.library.auth.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setMembershipDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByName(com.library.entity.ERole.ROLE_MEMBER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        // 保存到 Redis，设置过期时间（比如 1 小时）
        redisTemplate.opsForValue().set("TOKEN:" + loginRequest.getUsername(), token, Duration.ofHours(1));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("roles", userDetails.getAuthorities());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7); // 去掉 "Bearer "
        String username = jwtUtils.extractUsername(token);

        // 删除 Redis 中对应的 token
        String redisKey = "TOKEN:" + username;
        Boolean deleted = redisTemplate.delete(redisKey);

        return ResponseEntity.ok("Logged out successfully");
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthenticated");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 只返回安全字段，避免 password、关联实体带来的问题
        Map<String, Object> userInfo = Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "roles", user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(userInfo);
    }

}
