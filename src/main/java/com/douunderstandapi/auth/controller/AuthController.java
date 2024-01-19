package com.douunderstandapi.auth.controller;

import com.douunderstandapi.auth.dto.request.AuthEmailRequest;
import com.douunderstandapi.auth.dto.request.AuthLoginRequest;
import com.douunderstandapi.auth.dto.response.AuthEmailResponse;
import com.douunderstandapi.auth.dto.response.AuthLoginResponse;
import com.douunderstandapi.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest request,
                                                   HttpServletResponse httpServletResponse) {

        AuthLoginResponse res = authService.login(request, httpServletResponse);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/email")
    public ResponseEntity<AuthEmailResponse> authEmail(@RequestBody AuthEmailRequest request) {

        AuthEmailResponse res = authService.authEmail(request);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthLoginResponse> refresh(HttpServletRequest httpServletRequest,
                                                     HttpServletResponse httpServletResponse) {

        AuthLoginResponse res = authService.refresh(httpServletRequest, httpServletResponse);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest httpServletRequest,
                                          HttpServletResponse httpServletResponse) {

        Boolean res = authService.logout(httpServletRequest, httpServletResponse);
        return ResponseEntity.ok().body(res);
    }
}
