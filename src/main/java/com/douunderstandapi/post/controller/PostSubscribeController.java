package com.douunderstandapi.post.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.post.dto.response.PostSubscribeUpdateResponse;
import com.douunderstandapi.post.service.PostSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostSubscribeController {

    private final PostSubscribeService postSubscribeService;

    @PatchMapping("/{postId}")
    public ResponseEntity<PostSubscribeUpdateResponse> updatePostSubscribe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long postId,
            @RequestParam Boolean isSubscribe) {
        String email = userDetails.getUsername();
        PostSubscribeUpdateResponse response = postSubscribeService.updatePostSubscribe(
                email, postId, isSubscribe);
        return ResponseEntity.ok().body(response);
    }
}
