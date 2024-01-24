package com.douunderstandapi.post.controller;

import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import com.douunderstandapi.post.dto.request.PostAddRequest;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.post.dto.response.PostAddResponse;
import com.douunderstandapi.post.dto.response.PostGetResponse;
import com.douunderstandapi.post.dto.response.PostUpdateResponse;
import com.douunderstandapi.post.dto.response.PostsGetResponse;
import com.douunderstandapi.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostAddResponse> addPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @Validated @RequestBody PostAddRequest request) {
        String email = getEmailFromUserDetails(userDetails);
        PostAddResponse response = postService.addPost(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PostsGetResponse> getPosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int pageNumber,
            @RequestParam(required = false) String mode) {

        String email = getEmailFromUserDetails(userDetails);
        PostsGetResponse response = postService.findPosts(email, pageNumber, mode);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostGetResponse> getPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long postId) {
        String email = getEmailFromUserDetails(userDetails);
        PostGetResponse response = postService.findPost(email, postId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable Long postId,
                                                         @Validated @RequestBody PostUpdateRequest request) {
        String email = getEmailFromUserDetails(userDetails);
        PostUpdateResponse response = postService.update(email, postId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Boolean> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long postId) {
        String email = getEmailFromUserDetails(userDetails);
        Boolean response = postService.delete(email, postId);
        return ResponseEntity.ok().body(response);
    }

    private String getEmailFromUserDetails(UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }
}
