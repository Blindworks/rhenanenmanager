package com.blindworks.rhenanenmanager.service.impl;

import com.blindworks.rhenanenmanager.domain.dto.request.LoginRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.AuthResponse;
import com.blindworks.rhenanenmanager.domain.entity.User;
import com.blindworks.rhenanenmanager.domain.repository.UserRepository;
import com.blindworks.rhenanenmanager.security.JwtTokenProvider;
import com.blindworks.rhenanenmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        // Update last login time
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLogin(LocalDateTime.now());
        user.setFailedLogins(0);
        userRepository.save(user);

        String roleName = user.getRole() != null ? user.getRole().getName() : "NONE";

        log.info("User {} logged in successfully", loginRequest.getUsername());

        return new AuthResponse(token, user.getUsername(), user.getEmail(), roleName);
    }
}
