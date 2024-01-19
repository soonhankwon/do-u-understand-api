package com.douunderstandapi.subscribe.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.subscribe.dto.response.SubscribeAddResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribeCancelResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribePostsGetResponse;
import com.douunderstandapi.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @GetMapping
    public ResponseEntity<SubscribePostsGetResponse> getSubscribePosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int pageNumber) {
        String email = getEmailFromUserDetails(userDetails);

        SubscribePostsGetResponse res = subscribeService.getSubscribePosts(email, pageNumber);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<SubscribeAddResponse> addSubscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable Long postId) {
        String email = getEmailFromUserDetails(userDetails);

        SubscribeAddResponse res = subscribeService.addSubscribe(email, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<SubscribeCancelResponse> cancelSubscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                   @PathVariable Long postId) {
        String email = getEmailFromUserDetails(userDetails);
        SubscribeCancelResponse res = subscribeService.cancelSubscribe(email, postId);
        return ResponseEntity.ok().body(res);
    }

    private String getEmailFromUserDetails(UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }
}
