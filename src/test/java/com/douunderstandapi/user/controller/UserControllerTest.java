package com.douunderstandapi.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.dto.request.UserPasswordUpdateRequest;
import com.douunderstandapi.user.dto.response.UserAddResponse;
import com.douunderstandapi.user.dto.response.UserDeleteResponse;
import com.douunderstandapi.user.dto.response.UserPasswordUpdateResponse;
import com.douunderstandapi.user.enumType.UserStatus;
import com.douunderstandapi.user.service.UserService;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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
    @WithUserPrincipals
    @Test
    void addUser() throws Exception {
        JSONObject request = new JSONObject();
        request.put("email", "test@gmail.com");
        request.put("password", "password1!");
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

    @DisplayName("{PUT} 유저 패스워드 수정 요청 - 정상호출")
    @WithUserPrincipals
    @Test
    void updatePassword() throws Exception {
        JSONObject request = new JSONObject();
        request.put("password", "password1!");

        when(userService.updatePassword(anyString(), any(UserPasswordUpdateRequest.class)))
                .thenReturn(createUserPasswordUpdateResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/users/update")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "update-user-password",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("password")
                                                .type(STRING)
                                                .description("패스워드")),
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
                .andExpect(status().isOk());
    }

    public UserPasswordUpdateResponse createUserPasswordUpdateResponse() {
        return UserPasswordUpdateResponse.of(1L, "selfnews@gmail.ocm", true);
    }

    @DisplayName("{PATCH} 유저 탈퇴 요청 - 정상호출")
    @WithUserPrincipals
    @Test
    void deleteUser() throws Exception {
        when(userService.deleteUser(anyString(), anyString()))
                .thenReturn(createUserDeleteResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.patch("/api/v1/users/delete")
                                .param("code", UUID.randomUUID().toString())
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString()))
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
                                                .description("유저상태")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    private UserAddResponse createUserAddResponse() {
        return UserAddResponse.of(1L, "selfnews@gmail.com", true);
    }

    private UserDeleteResponse createUserDeleteResponse() {
        return UserDeleteResponse.of(1L, "selfnews@email.com", UserStatus.DELETED);
    }
}