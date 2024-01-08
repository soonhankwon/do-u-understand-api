package com.douunderstandapi.common.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UserBaseTimeEntityTest {

    @Test
    void setDeletedAtNow() {
        UserBaseTimeEntity userBaseTimeEntity = new UserBaseTimeEntity();

        userBaseTimeEntity.setDeletedAtNow();

        assertThat(userBaseTimeEntity.getDeletedAt())
                .isBefore(LocalDateTime.now());
    }

    @Test
    void getter() {
        UserBaseTimeEntity userBaseTimeEntity = new UserBaseTimeEntity();

        assertThat(userBaseTimeEntity.getCreatedAt()).isNull();
        assertThat(userBaseTimeEntity.getModifiedAt()).isNull();
    }
}