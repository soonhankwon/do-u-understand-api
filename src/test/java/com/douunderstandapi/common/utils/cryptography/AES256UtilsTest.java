package com.douunderstandapi.common.utils.cryptography;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("[Utils] AES256 알고리즘 암호화/복호화 테스트")
@SpringBootTest(
        classes = {AES256Utils.class}
)
class AES256UtilsTest {

    @Test
    void encryptAndDecryptAES256() {
        String plainText = "test@gmail.com";
        String encodedText = AES256Utils.encryptAES256(plainText);
        String decodedText = AES256Utils.decryptAES256(encodedText);

        assertThat(encodedText).isNotEqualTo(plainText);
        assertThat(decodedText).isEqualTo(plainText);
    }
}