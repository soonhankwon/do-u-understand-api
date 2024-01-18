package com.douunderstandapi.user.service;

import com.douunderstandapi.auth.repository.redis.AuthEmailCodeRepository;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.dto.response.UserAddResponse;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailUtils emailUtils;
    private final AuthEmailCodeRepository authEmailCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAddResponse addUser(UserAddRequest request) {
        //test
        if (request.code().equals("1")) {
            User user = request.toEntity(passwordEncoder::encode);
            userRepository.save(user);
            return UserAddResponse.from(user);
        }
        String inputCode = request.code();
        //TODO inputCode validation 은 DTO 에서 하도록 수정 Early Exception
        if (inputCode == null || inputCode.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AUTH_CODE);
        }
        // Redis Cache 에서 인증코드를 찾고 없다면 exception
        String email = request.email();
        String code = authEmailCodeRepository
                .get(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_AUTH_CODE));

        if (!inputCode.equals(code)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AUTH_CODE);
        }

        User user = request.toEntity(passwordEncoder::encode);
        userRepository.save(user);
        // user 저장 후 캐시 삭제
        authEmailCodeRepository.delete(email);
        return UserAddResponse.from(user);
    }

    public UserDeleteResponse deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        // soft delete
        user.delete();
        return UserDeleteResponse.from(user);
    }
}
