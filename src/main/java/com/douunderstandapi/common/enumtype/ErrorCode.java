package com.douunderstandapi.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //400

    //500
    UNKNOWN(5000, "서버 내부 에러가 발생했습니다.");

    private final int code;
    private final String msg;
}
