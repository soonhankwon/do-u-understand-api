package com.douunderstandapi.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.user.domain.dto.request.UserAddRequest;
import com.douunderstandapi.user.domain.dto.request.UserEmailAuthRequest;
import com.douunderstandapi.user.domain.dto.response.UserAddResponse;
import com.douunderstandapi.user.domain.dto.response.UserEmailAuthResponse;
import com.douunderstandapi.user.service.UserService;
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

@DisplayName("[컨트롤러] - 유저")
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private UserService userService;

    @DisplayName("{POST} 회원등록 - 정상호출")
    @Test
    void addUser() throws Exception {
        JSONObject request = new JSONObject();
        request.put("email", "test@gmail.com");
        request.put("password", "1234");
        request.put("code", "5b86d3dc-f0c4-4226-b684-c1ca250b7c21");
        request.put("isAllowedNotification", true);

        when(userService.addUser(any(UserAddRequest.class)))
                .thenReturn(createUserAddResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "create-user",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("password").type(STRING).description("패스워드"),
                                        fieldWithPath("code").type(STRING).description("인증코드"),
                                        fieldWithPath("isAllowedNotification").type(BOOLEAN).description("알람설정여부")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("회원 ID"),
                                        fieldWithPath("email")
                                                .type(STRING)
                                                .description("이메일"),
                                        fieldWithPath("isAllowedNotification")
                                                .type(BOOLEAN)
                                                .description("알람설정여부")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("{POST} 이메일 인증요청 - 정상호출")
    @Test
    void authUserEmail() throws Exception {
        JSONObject request = new JSONObject();
        request.put("email", "test@gmail.com");

        when(userService.authUserEmail(any(UserEmailAuthRequest.class)))
                .thenReturn(createUserEmailAuthResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/users/auth-email")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "auth-user-email",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일")
                                ),
                                responseFields(
                                        fieldWithPath("code")
                                                .type(STRING)
                                                .description("인증코드")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    private UserAddResponse createUserAddResponse() {
        return UserAddResponse.of(1L, "tester@gmail.com", true);
    }

    private UserEmailAuthResponse createUserEmailAuthResponse() {
        return UserEmailAuthResponse.from("5b86d3dc-f0c4-4226-b684-c1ca250b7c21");
    }
}