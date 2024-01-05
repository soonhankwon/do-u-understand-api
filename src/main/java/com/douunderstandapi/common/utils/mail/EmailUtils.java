package com.douunderstandapi.common.utils.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailUtils {

    @Value("${spring.mail.username}")
    private String username;

    private static final String DOMAIN_NAME = "DO-U-UNDERSTAND";
    private static final String MORNING = "Morning";
    private static final String AFTERNOON = "Afternoon";
    private static final String EVENING = "Evening";

    private final JavaMailSender javaMailSender;

    public String sendSimpleMessage(String targetEmail, String subject, String content, String link) {
        try {
            MimeMessage message = createMessage(targetEmail, subject, content, link);
            javaMailSender.send(message);
            return "success";
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    private MimeMessage createMessage(String targetEmail, String subject, String content, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        // targetEmail 보내는 대상
        message.addRecipients(MimeMessage.RecipientType.TO, targetEmail);
        message.setSubject(DOMAIN_NAME + " " + subject); //메일 제목

        // 메일 내용 만들기 html text
        String msg = createMailMessage(subject, content, link);

        // subtype: html
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(username, DOMAIN_NAME)); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    private String createMailMessage(String subject, String content, String link) {
        LocalTime now = LocalTime.now();
        String timeTextNow = findTimeTextByLocalTimeNow(now);
        String msg = "<html><body style=\"font-family: 'Arial', sans-serif;\">";
        msg += "<div style=\"background-color: #F4F4F4; padding: 30px; border-radius: 10px;\">";
        msg += "<h1 style=\"font-size: 24px; color: #333;\">" + "[" + timeTextNow + "]" + subject + "</h1>";
        msg += "<p style=\"font-size: 16px; color: #666;\">" + content + "</p>";
        msg += "<p style=\"font-size: 16px; color: #666;\">아래 관련 링크에서 컨텐츠를 확인해보세요.</p>";
        msg += "<div style=\"margin-top: 20px;\"><a href=\"" + link
                + "\" style=\"display: inline-block; padding: 12px 24px; background-color: #007BFF; color: #fff; text-decoration: none; border-radius: 5px;\">컨텐츠 확인하기</a></div>";
        msg += "</div></body></html>";
        return msg;
    }

    private static String findTimeTextByLocalTimeNow(LocalTime now) {
        if (now.isBefore(LocalTime.of(12, 59))) {
            return MORNING;
        } else if (now.isBefore(LocalTime.of(19, 59))) {
            return AFTERNOON;
        } else {
            return EVENING;
        }
    }
}
