package com.douunderstandapi.post.service;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.dto.response.PostSubscribeUpdateResponse;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostSubscribeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostSubscribeUpdateResponse updatePostSubscribe(String email, Long knowledgeId,
                                                           Boolean isSubscribe) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository
                .findById(knowledgeId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        // 자신이 작성한 지식이 아니라면 구독 거부
        post.validateAccessAuth(user);
        post.updateSubscribeStatus(isSubscribe);
        return PostSubscribeUpdateResponse.from(post);
    }
}
