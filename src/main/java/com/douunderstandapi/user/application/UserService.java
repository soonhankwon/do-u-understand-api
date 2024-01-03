package com.douunderstandapi.user.application;

import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.domain.dto.request.UserAddRequest;
import com.douunderstandapi.user.domain.dto.response.UserAddResponse;
import com.douunderstandapi.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserAddResponse addUser(UserAddRequest request) {
        User user = request.toEntity();
        userRepository.save(user);

        return UserAddResponse.from(user);
    }
}
