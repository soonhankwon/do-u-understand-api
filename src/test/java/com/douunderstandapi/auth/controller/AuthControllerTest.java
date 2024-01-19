package com.douunderstandapi.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.annotation.WithUserPrincipals;
import com.douunderstandapi.auth.dto.request.AuthEmailRequest;
import com.douunderstandapi.auth.dto.request.AuthLoginRequest;
import com.douunderstandapi.auth.dto.response.AuthEmailResponse;
import com.douunderstandapi.auth.dto.response.AuthLoginResponse;
import com.douunderstandapi.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[컨트롤러] - 인증")
@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs
class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private AuthService authService;

    @DisplayName("{POST} 로그인 - 정상호출")
    @WithUserPrincipals
    @Test
    void login() throws Exception {
        Long id = 1L;
        String email = "test@gmail.com";
        JSONObject request = new JSONObject();
        request.put("email", email);
        request.put("password", "password1!");

        when(authService.login(any(AuthLoginRequest.class), any(HttpServletResponse.class)))
                .thenReturn(createAuthLoginResponse(id, email));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/auth/login")
                                .with(csrf().asHeader())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "auth-login",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("password").type(STRING).description("패스워드")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("유저 ID"),
                                        fieldWithPath("email")
                                                .type(STRING)
                                                .description("이메일"),
                                        fieldWithPath("Authorization")
                                                .type(STRING)
                                                .description("AccessToken")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{POST} 인증메일 발송 - 정상호출")
    @WithUserPrincipals
    @Test
    void authEmail() throws Exception {
        String email = "test@gmail.com";
        JSONObject request = new JSONObject();
        request.put("email", email);

        when(authService.authEmail(any(AuthEmailRequest.class)))
                .thenReturn(createAuthEmailResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/auth/email")
                                .with(csrf().asHeader())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "auth-email",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일")
                                ),
                                responseFields(
                                        fieldWithPath("code")
                                                .type(STRING)
                                                .description("이메일 인증 코드")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{POST} 토큰 리프레시 - 정상호출")
    @WithUserPrincipals
    @Test
    void refresh() throws Exception {
        Long id = 1L;
        String email = "test@gmail.com";

        when(authService.refresh(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .thenReturn(createAuthLoginResponse(id, email));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/auth/refresh")
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "auth-refresh",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("유저 ID"),
                                        fieldWithPath("email")
                                                .type(STRING)
                                                .description("이메일"),
                                        fieldWithPath("Authorization")
                                                .type(STRING)
                                                .description("AccessToken")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    private AuthLoginResponse createAuthLoginResponse(Long id, String email) {
        return new AuthLoginResponse(id, email, UUID.randomUUID().toString());
    }

    private AuthEmailResponse createAuthEmailResponse() {
        return new AuthEmailResponse(UUID.randomUUID().toString());
    }

    @Test
    void logout() {
    }
}