package com.douunderstandapi.like.service;

import com.douunderstandapi.like.domain.Like;
import com.douunderstandapi.like.dto.response.LikePostsGetResponse;
import com.douunderstandapi.like.repository.LikeRepository;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.dto.PostDTO;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikePostsGetResponse getLikes(String email, int pageNumber) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid email"));

        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Like> page = likeRepository.findAllByUser(user, pageable);

        List<PostDTO> articleDTOS = page.getContent()
                .stream()
                .map(Like::getPost)
                .map(PostDTO::from)
                .collect(Collectors.toList());

        int totalPages = page.getTotalPages();
        return new LikePostsGetResponse(totalPages, articleDTOS);
    }

    @Transactional
    public Boolean addLike(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid email"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("invalid postId"));

        Optional<Like> optionalLike = likeRepository.findByUserAndPost(user, post);
        if (optionalLike.isPresent()) {
            throw new RuntimeException("duplicated like");
        } else {
            Like like = new Like(user, post);
            likeRepository.save(like);
        }
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean cancelLike(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid email"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("invalid postId"));

        likeRepository.deleteByPostAndUser(post, user);
        return Boolean.TRUE;
    }
}
