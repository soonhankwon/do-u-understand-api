package com.douunderstandapi.user.controller;

import com.douunderstandapi.user.domain.dto.request.UserAddRequest;
import com.douunderstandapi.user.domain.dto.request.UserEmailAuthRequest;
import com.douunderstandapi.user.domain.dto.response.UserAddResponse;
import com.douunderstandapi.user.domain.dto.response.UserEmailAuthResponse;
import com.douunderstandapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserAddResponse> addUser(@RequestBody UserAddRequest request) {
        UserAddResponse response = userService.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 회원가입시 이메일인증 번호 확인 후 가입 엔드포인트에 유효한 인증번호를 입력해야 정상 가입 가능
    @PostMapping("/auth-email")
    public ResponseEntity<UserEmailAuthResponse> authUserEmail(@RequestBody UserEmailAuthRequest request) {
        UserEmailAuthResponse response = userService.authUserEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
