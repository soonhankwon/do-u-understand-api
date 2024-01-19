package com.douunderstandapi.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //400
    NOT_EXIST_USER_EMAIL(4000, "유저 이메일이 존재하지 않습니다."),
    NOT_EXIST_POST_ID(4001, "게시글 아이디가 존재하지 않습니다."),
    ENCRYPTION_FAILED(4002, "암호화에 실패하였습니다."),
    DECRYPTION_FAILED(4003, "복호화에 실패하였습니다."),
    CRYPTOGRAPHY_FAILED(4004, "암복호화에 실패하였습니다"),
    NOT_EXIST_AUTH_CODE(4005, "해당 이메일의 인증코드가 존재하지 않습니다."),
    INVALID_AUTH_CODE(4006, "유효하지 않은 인증코드 입니다."),
    NOT_MATCHED_EMAIL_AND_PASSWORD(4007, "이메일과 패스워드가 일치하지 않습니다."),
    INVALID_TOKEN(4008, "유효한 토큰이 아닙니다."),
    LOGIN_REQUIRED_FIRST(4009, "로그인이 필요합니다."),
    NO_AUTH_ACCESS(4010, "접근할 수 있는 권한이 없습니다."),
    NOT_EXIST_COOKIE(4010, "쿠키가 없습니다. 로그인을 해주세요."),
    COOKIE_EXPIRATION(4011, "만료된 쿠키입니다."),
    DUPLICATED_SUBSCRIBE(4012, "중복된 구독 요청입니다."),

    //500
    UNKNOWN(5000, "서버 내부 에러가 발생했습니다."),
    REDIS_CONNECTION_DOWN(5001, "레디스 서버가 다운된 상태입니다.");
    private final int code;
    private final String msg;
}
