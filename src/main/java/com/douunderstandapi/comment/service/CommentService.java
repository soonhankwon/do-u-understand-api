package com.douunderstandapi.comment.service;

import com.douunderstandapi.comment.domain.Comment;
import com.douunderstandapi.comment.dto.CommentDTO;
import com.douunderstandapi.comment.dto.request.CommentAddRequest;
import com.douunderstandapi.comment.dto.request.CommentDeleteRequest;
import com.douunderstandapi.comment.dto.response.CommentAddResponse;
import com.douunderstandapi.comment.dto.response.CommentDeleteResponse;
import com.douunderstandapi.comment.dto.response.CommentsGetResponse;
import com.douunderstandapi.comment.repository.CommentRepository;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentsGetResponse getComments(String email, Long postId) {
        assert email != null;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));
        
        post.validateAccessAuth(user);

        List<CommentDTO> commentDTOs = commentRepository.findAllByPost(post)
                .stream()
                .map(CommentDTO::of)
                .toList();

        return new CommentsGetResponse(commentDTOs);
    }

    @Transactional
    public CommentAddResponse addComment(String email, CommentAddRequest commentAddRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Long postId = commentAddRequest.postId();
        assert postId != null;

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        Comment comment = commentAddRequest.toEntity(commentAddRequest, user, post);
        commentRepository.save(comment);

        return CommentAddResponse.from(comment);
    }

    @Transactional
    public CommentDeleteResponse deleteComment(String email, CommentDeleteRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Long commentId = request.commentId();
        assert commentId != null;

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_COMMENT_ID));

        comment.validateDeleteAuth(user);

        commentRepository.delete(comment);
        return CommentDeleteResponse.from(comment);
    }
}
