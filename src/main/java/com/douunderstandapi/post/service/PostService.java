package com.douunderstandapi.post.service;

import com.douunderstandapi.category.domain.Category;
import com.douunderstandapi.category.repository.CategoryRepository;
import com.douunderstandapi.comment.repository.CommentRepository;
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
import com.douunderstandapi.post.enumType.PostStatus;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.subscribe.domain.Subscribe;
import com.douunderstandapi.subscribe.repository.SubscribeRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
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
    private final SubscribeRepository subscribeRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    public PostAddResponse addPost(String email, PostAddRequest request) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        String categoryName = request.categoryNames().getFirst();
        Category category;
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isPresent()) {
            category = optionalCategory.get();
        } else {
            category = new Category(categoryName);
            categoryRepository.save(category);
        }

        Post post = request.toEntity(user, category);
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
        Long count = commentRepository.countAllByPost(post);
        assert count != null;

        return PostGetResponse.of(post, count);
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

        Optional<Subscribe> optionalSubscribe = subscribeRepository.findByUserAndPost(user, post);
        if (optionalSubscribe.isPresent()) {
            return PostUpdateResponse.of(post, true);
        }
        return PostUpdateResponse.of(post, false);
    }

    public Boolean delete(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        // 자신이 작성한 지식이 아니라면 삭제 거부
        post.validateAccessAuth(user);
        subscribeRepository.deleteAllByPost(post);
        postRepository.delete(post);

        return Boolean.TRUE;
    }

    public PostsGetResponse findPosts(String email, int pageNumber, String mode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Post> page;
        if (mode.equals("all")) {
            page = postRepository.findAllByPostStatus(PostStatus.NOTICE, pageable);
        } else {
            page = postRepository.findAllByUser(user, pageable);
        }

        int totalPages = page.getTotalPages();
        List<Post> posts = page.getContent();

        List<PostDTO> postDTO = getPostDTO(posts, user);
        return new PostsGetResponse(totalPages, postDTO);
    }

    private List<PostDTO> getPostDTO(List<Post> posts, User user) {
        return posts.stream()
                .map(p -> {
                    Long commentCount = commentRepository.countAllByPost(p);
                    Optional<Subscribe> optionalSubscribe = subscribeRepository.findByUserAndPost(user, p);
                    if (optionalSubscribe.isPresent()) {
                        return PostDTO.of(p, commentCount, true);
                    } else {
                        return PostDTO.of(p, commentCount, false);
                    }
                })
                .collect(Collectors.toList());
    }
}
