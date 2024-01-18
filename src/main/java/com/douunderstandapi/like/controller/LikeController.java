package com.douunderstandapi.like.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.like.dto.response.LikePostsGetResponse;
import com.douunderstandapi.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public ResponseEntity<LikePostsGetResponse> getLikes(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestParam int pageNumber) {
        String email = getEmailFromUserDetails(userDetails);

        LikePostsGetResponse res = likeService.getLikes(email, pageNumber);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<Boolean> addLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long postId) {
        String email = getEmailFromUserDetails(userDetails);

        Boolean res = likeService.addLike(email, postId);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/cancel/{postId}")
    public ResponseEntity<Boolean> cancelLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long postId) {
        String email = getEmailFromUserDetails(userDetails);
        Boolean res = likeService.cancelLike(email, postId);
        return ResponseEntity.ok().body(res);
    }


    private String getEmailFromUserDetails(UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }
}
