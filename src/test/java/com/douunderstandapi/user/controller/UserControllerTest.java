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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.annotation.WithUserPrincipals;
import com.douunderstandapi.auth.dto.request.AuthEmailRequest;
import com.douunderstandapi.auth.service.AuthService;
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.dto.response.UserAddResponse;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.enumType.UserStatus;
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

    @MockBean
    private AuthService authService;

    @DisplayName("{POST} 회원등록 - 정상호출")
    @WithUserPrincipals
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
                                .with(csrf().asHeader())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "add-user",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("password").type(STRING).description("패스워드"),
                                        fieldWithPath("code").type(STRING).description("인증코드"),
                                        fieldWithPath("isAllowedNotification").type(BOOLEAN).description("알람설정여부")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("{PATCH} 유저 탈퇴 요청 - 정상호출")
    @WithUserPrincipals
    @Test
    void deleteUser() throws Exception {
        when(userService.deleteUser(any(String.class)))
                .thenReturn(createUserDeleteResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.patch("/api/v1/users/delete")
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "delete-user",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("회원 ID"),
                                        fieldWithPath("email")
                                                .type(STRING)
                                                .description("이메일"),
                                        fieldWithPath("userStatus")
                                                .type(STRING)
                                                .description("DELETED")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    private UserAddResponse createUserAddResponse() {
        return UserAddResponse.of(1L, "test@gmail.com", true);
    }

    private AuthEmailRequest createUserEmailAuthResponse() {
        return new AuthEmailRequest("5b86d3dc-f0c4-4226-b684-c1ca250b7c21");
    }

    private UserDeleteResponse createUserDeleteResponse() {
        return UserDeleteResponse.of(1L, "test@email.com", UserStatus.DELETED);
    }
}