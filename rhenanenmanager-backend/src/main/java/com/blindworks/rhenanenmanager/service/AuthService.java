package com.blindworks.rhenanenmanager.service;

import com.blindworks.rhenanenmanager.domain.dto.request.LoginRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
}
