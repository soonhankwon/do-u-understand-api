package com.douunderstandapi.common.utils.mail;

import com.douunderstandapi.common.utils.mail.dto.AuthEmailDTO;
import com.douunderstandapi.common.utils.mail.dto.NotificationEmailDTO;
import com.douunderstandapi.common.utils.mail.dto.PasswordRefreshEmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.UUID;
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

    private static final String DOMAIN_NAME = "SELFnews";
    private static final String MORNING = "Morning";
    private static final String AFTERNOON = "Afternoon";
    private static final String EVENING = "Evening";

    private final JavaMailSender javaMailSender;

    public String sendEmailAuthMessage(String targetEmail) {
        try {
            AuthEmailDTO dto = AuthEmailDTO.from(EmailUtils::createCode);
            MimeMessage message = createMessage(targetEmail, dto);
            javaMailSender.send(message);
            return dto.code();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    public String sendPasswordRefreshMessage(String targetEmail, String password) {
        try {
            PasswordRefreshEmailDTO dto = PasswordRefreshEmailDTO.from(password);
            MimeMessage message = createMessage(targetEmail, dto);
            javaMailSender.send(message);
            return dto.code();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    public String sendPostNotificationMessage(String targetEmail, NotificationEmailDTO dto) {
        try {
            MimeMessage message = createMessage(targetEmail, dto);
            javaMailSender.send(message);
            return "success";
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    private <T> MimeMessage createMessage(String targetEmail, T t)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        // targetEmail 보내는 대상
        message.addRecipients(MimeMessage.RecipientType.TO, targetEmail);
        String msg = null;

        assert t != null;
        if (t instanceof AuthEmailDTO dto) {
            message.setSubject(DOMAIN_NAME + " " + dto.title()); //메일 제목
            // 인증 메일 내용 만들기 html text
            msg = createAuthEmailMessage(dto.code());
        }
        if (t instanceof PasswordRefreshEmailDTO dto) {
            message.setSubject(DOMAIN_NAME + " " + dto.title()); //메일 제목
            // 임시 비밀번호 발급 메일 내용 만들기 html text
            msg = createPasswordRefreshEmailMessage(dto.code());
        }
        if (t instanceof NotificationEmailDTO dto) {
            message.setSubject(DOMAIN_NAME + " " + dto.title()); //메일 제목
            // 노티 메일 내용 만들기 html text
            msg = createNotificationMailMessage(dto);
        }

        // subtype: html
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(username, DOMAIN_NAME)); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    private String createAuthEmailMessage(String code) {
        return String.format("<html><body style=\"font-family: 'Arial', sans-serif;\">"
                + "<div style=\"background-color: #F4F4F4; padding: 30px; border-radius: 10px;\">"
                + "<h1 style=\"font-size: 24px; color: #333;\">[회원가입 인증 코드]%s</h1>"
                + "<p style=\"font-size: 16px; color: #666;\">아래 인증번호를 확인 후 복사해서 입력해주세요.</p>"
                + "%s"
                + "</td></tr></tbody></table></div></body></html>", DOMAIN_NAME, code);
    }

    private String createPasswordRefreshEmailMessage(String code) {
        return String.format("<html><body style=\"font-family: 'Arial', sans-serif;\">"
                + "<div style=\"background-color: #F4F4F4; padding: 30px; border-radius: 10px;\">"
                + "<h1 style=\"font-size: 24px; color: #333;\">[임시 비밀번호]%s</h1>"
                + "<p style=\"font-size: 16px; color: #666;\">아래 임시 비밀번호를 확인 후 로그인해주세요. 로그인 후 비밀번호를 재설정해주세요.</p>"
                + "%s"
                + "</td></tr></tbody></table></div></body></html>", DOMAIN_NAME, code);
    }

    private String createNotificationMailMessage(NotificationEmailDTO dto) {
        LocalTime now = LocalTime.now();
        String timeTextNow = findTimeTextByLocalTimeNow(now);
        String msg = "<html><body style=\"font-family: 'Arial', sans-serif;\">";
        msg += "<div style=\"background-color: #F4F4F4; padding: 30px; border-radius: 10px;\">";
        msg += "<h1 style=\"font-size: 24px; color: #333;\">" + "[" + timeTextNow + "]" + dto.title() + "</h1>";
        msg += "<p class=\"whitespace:pre-line\" style=\"font-size: 16px; color: #666;\">" + dto.content() + "</p>";
        msg += "<p style=\"font-size: 16px; color: #666;\">아래 관련 링크에서 컨텐츠를 확인해보세요.</p>";
        msg += "<div style=\"margin-top: 20px;\"><a href=\"" + dto.link()
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

    private static String createCode() {
        return UUID.randomUUID().toString();
    }
}
