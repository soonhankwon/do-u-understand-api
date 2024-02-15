package com.douunderstandapi.comment.controller;

import com.douunderstandapi.comment.dto.request.CommentAddRequest;
import com.douunderstandapi.comment.dto.request.CommentDeleteRequest;
import com.douunderstandapi.comment.dto.response.CommentAddResponse;
import com.douunderstandapi.comment.dto.response.CommentDeleteResponse;
import com.douunderstandapi.comment.dto.response.CommentsGetResponse;
import com.douunderstandapi.comment.service.CommentService;
import com.douunderstandapi.common.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/{postId}")
    public ResponseEntity<CommentsGetResponse> getComments(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long postId) {
        String email = getEmailFromUserDetails(userDetails);
        CommentsGetResponse res = commentService.findComments(email, postId);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping
    public ResponseEntity<CommentAddResponse> addComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @Validated @RequestBody CommentAddRequest commentAddRequest) {
        String email = getEmailFromUserDetails(userDetails);
        CommentAddResponse res = commentService.addComment(email, commentAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping
    public ResponseEntity<CommentDeleteResponse> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @Validated @RequestBody CommentDeleteRequest request) {
        String email = getEmailFromUserDetails(userDetails);

        CommentDeleteResponse res = commentService.deleteComment(email, request);
        return ResponseEntity.ok().body(res);
    }

    private String getEmailFromUserDetails(UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }
}
