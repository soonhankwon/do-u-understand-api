package com.douunderstandapi.common.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.douunderstandapi.common.utils.cryptography.AES256Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("[Converter] AES256 Convertor 테스트")
@SpringBootTest(classes = {AES256Utils.class})
class AES256ToStringConverterTest {

    @Test
    @DisplayName("DB 데이터 컬럼으로 암호화 테스트")
    void convertToDatabaseColumn() {
        String attribute = "test@gmail.com";
        AES256ToStringConverter converter = new AES256ToStringConverter();
        String encodedAttribute = converter.convertToDatabaseColumn(attribute);

        assertThat(encodedAttribute).isNotEqualTo(attribute);
    }

    @Test
    @DisplayName("DB 데이터를 애플리케이션 엔티티 프로퍼티로 복호화 테스트")
    void convertToEntityAttribute() {
        String attribute = "test@gmail.com";
        AES256ToStringConverter converter = new AES256ToStringConverter();
        String decodedAttribute = converter.convertToEntityAttribute("H7ATa07P3z6BYWv4eVEAwg==");

        assertThat(decodedAttribute).isEqualTo(attribute);
    }
}