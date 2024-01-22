package com.douunderstandapi.user.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.dto.request.UserPasswordUpdateRequest;
import com.douunderstandapi.user.dto.response.UserAddResponse;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.dto.response.UserPasswordUpdateResponse;
import com.douunderstandapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PutMapping("/update")
    public ResponseEntity<UserPasswordUpdateResponse> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserPasswordUpdateRequest request) {
        String email = userDetails.getUsername();
        UserPasswordUpdateResponse response = userService.updatePassword(email, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/delete")
    public ResponseEntity<UserDeleteResponse> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestParam(value = "code") String authCode) {
        String email = userDetails.getUsername();
        UserDeleteResponse response = userService.deleteUser(email, authCode);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
