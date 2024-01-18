package com.douunderstandapi.post.service;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.dto.PostDTO;
import com.douunderstandapi.post.dto.request.PostAddRequest;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.post.dto.response.PostAddResponse;
import com.douunderstandapi.post.dto.response.PostGetResponse;
import com.douunderstandapi.post.dto.response.PostUpdateResponse;
import com.douunderstandapi.post.dto.response.PostsGetResponse;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostAddResponse addPost(String email, PostAddRequest request) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = request.toEntity(user);
        postRepository.save(post);

        return PostAddResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostGetResponse findPost(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        // 자신이 작성한 지식이 아니라면 조회 거부
        post.validateAccessAuth(user);

        return PostGetResponse.from(post);
    }

    public PostUpdateResponse update(String email, Long postId, PostUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        // 자신이 작성한 지식이 아니라면 업데이트 거부
        post.validateAccessAuth(user);
        post.update(request);
        return PostUpdateResponse.from(post);
    }

    public String delete(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        // 자신이 작성한 지식이 아니라면 삭제 거부
        post.validateAccessAuth(user);
        postRepository.delete(post);

        return "deleted";
    }

    public PostsGetResponse findPostsByUser(String email, int pageNumber) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Post> page = postRepository.findAllByUser(user, pageable);

        int totalPages = page.getTotalPages();
        List<Post> posts = page.getContent();
        List<PostDTO> postDTO = getPostDTO(posts);

        return new PostsGetResponse(totalPages, postDTO);
    }

    private List<PostDTO> getPostDTO(List<Post> posts) {
        return posts.stream()
                .map(PostDTO::from)
                .collect(Collectors.toList());
    }
}
