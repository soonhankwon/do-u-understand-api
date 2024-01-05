package com.douunderstandapi.common.utils.mail;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailUtilsTest {

    @Autowired
    private EmailUtils emailUtils;

    @Test
    @DisplayName("Email 전송 테스트")
    void sendSimpleMessage() {
        String email = "sooable@gmail.com";
        String subject = "subject";
        String content = "content";
        String link = "link";

        String res = emailUtils.sendSimpleMessage(email, subject, content, link);

        assertThat(res).isEqualTo("success");
    }
}