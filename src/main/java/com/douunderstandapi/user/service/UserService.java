package com.douunderstandapi.user.service;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.common.utils.mail.EmailUtils;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.domain.dto.request.UserAddRequest;
import com.douunderstandapi.user.domain.dto.request.UserEmailAuthRequest;
import com.douunderstandapi.user.domain.dto.response.UserAddResponse;
import com.douunderstandapi.user.domain.dto.response.UserEmailAuthResponse;
import com.douunderstandapi.user.repository.UserRepository;
import com.douunderstandapi.user.repository.redis.UserEmailAuthCodeRepository;
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
    private final UserEmailAuthCodeRepository userEmailAuthCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAddResponse addUser(UserAddRequest request) {
        String inputCode = request.code();
        //TODO inputCode validation 은 DTO 에서 하도록 수정 Early Exception
        if (inputCode == null || inputCode.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AUTH_CODE);
        }
        // Redis Cache 에서 인증코드를 찾고 없다면 exception
        String email = request.email();
        String code = userEmailAuthCodeRepository
                .get(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_AUTH_CODE));

        if (!inputCode.equals(code)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AUTH_CODE);
        }

        User user = request.toEntity(passwordEncoder::encode);
        userRepository.save(user);
        // user 저장 후 캐시 삭제
        userEmailAuthCodeRepository.delete(email);
        return UserAddResponse.from(user);
    }

    public UserEmailAuthResponse authUserEmail(UserEmailAuthRequest request) {
        String email = request.email();
        String code = emailUtils.sendEmailAuthMessage(email);

        // 해당 이메일의 인증코드가 이미 존재한다면 캐시 삭제하고 재발급 & 캐싱
        userEmailAuthCodeRepository.put(email, code);
        return UserEmailAuthResponse.from(code);
    }
}
