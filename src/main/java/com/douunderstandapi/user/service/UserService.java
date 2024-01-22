package com.douunderstandapi.user.service;

import com.douunderstandapi.auth.repository.redis.AuthEmailCodeRepository;
import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.dto.request.UserPasswordUpdateRequest;
import com.douunderstandapi.user.dto.response.UserAddResponse;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.dto.response.UserPasswordUpdateResponse;
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
    private final AuthEmailCodeRepository authEmailCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAddResponse addUser(UserAddRequest request) {
        String email = request.email();
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorCode.DUPLICATED_EMAIL);
        }
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

    @Transactional
    public UserDeleteResponse deleteUser(String email, String authCode) {
        String code = authEmailCodeRepository
                .get(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_AUTH_CODE));

        if (!authCode.equals(code)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AUTH_CODE);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));
        // soft delete
        user.delete();
        authEmailCodeRepository.delete(email);
        return UserDeleteResponse.from(user);
    }

    @Transactional
    public UserPasswordUpdateResponse updatePassword(String email, UserPasswordUpdateRequest request) {
        String code = authEmailCodeRepository
                .get(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_AUTH_CODE));

        String inputCode = request.authCode();
        if (!inputCode.equals(code)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AUTH_CODE);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        user.updatePassword(request.password(), passwordEncoder::encode);
        authEmailCodeRepository.delete(email);
        return UserPasswordUpdateResponse.from(user);
    }
}
