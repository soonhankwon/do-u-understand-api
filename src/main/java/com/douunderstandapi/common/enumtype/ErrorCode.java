package com.douunderstandapi.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //400
    NOT_EXIST_USER_EMAIL(4000, "유저 이메일이 존재하지 않습니다."),
    NOT_EXIST_KNOWLEDGE_ID(4001, "지식 아이디가 존재하지 않습니다."),

    //500
    UNKNOWN(5000, "서버 내부 에러가 발생했습니다.");

    private final int code;
    private final String msg;
}
