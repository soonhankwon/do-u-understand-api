package com.douunderstandapi.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.annotation.WithUserPrincipals;
import com.douunderstandapi.post.dto.request.PostAddRequest;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.post.dto.response.PostAddResponse;
import com.douunderstandapi.post.dto.response.PostGetResponse;
import com.douunderstandapi.post.dto.response.PostUpdateResponse;
import com.douunderstandapi.post.service.PostService;
import java.time.LocalDateTime;
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

@DisplayName("[컨트롤러] - 포스트")
@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
class PostControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private PostService postService;

    @DisplayName("{POST} 포스트 등록 - 정상호출")
    @WithUserPrincipals
    @Test
    void addPost() throws Exception {
        JSONObject request = new JSONObject();
        request.put("title", "RESTful API");
        request.put("content", "Restful API란.....");
        request.put("link", "https://abcdefssss/2in2");

        when(postService.addPost(anyString(), any(PostAddRequest.class)))
                .thenReturn(createPostAddResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/posts")
                                .with(csrf().asHeader())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "create-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("제목"),
                                        fieldWithPath("content").type(STRING).description("컨텐츠"),
                                        fieldWithPath("link").type(STRING).description("관련링크")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("지식 ID"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("컨텐츠"),
                                        fieldWithPath("link")
                                                .type(STRING)
                                                .description("관련링크"),
                                        fieldWithPath("isUnderstand")
                                                .type(BOOLEAN)
                                                .description("이해여부")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("{GET} 포스트 조회(상세) - 정상호출")
    @WithUserPrincipals
    @Test
    void getPost() throws Exception {
        when(postService.findPost(anyString(), any(Long.class)))
                .thenReturn(createPostDetailGetResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andDo(
                        document(
                                "get-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("postDTO").type(OBJECT).description("게시글 정보"),
                                        fieldWithPath("postDTO.id")
                                                .type(NUMBER)
                                                .description("게시글 ID"),
                                        fieldWithPath("postDTO.title")
                                                .type(STRING)
                                                .description("제목"),
                                        fieldWithPath("postDTO.content")
                                                .type(STRING)
                                                .description("컨텐츠"),
                                        fieldWithPath("postDTO.link")
                                                .type(STRING)
                                                .description("관련링크"),
                                        fieldWithPath("postDTO.isUnderstand")
                                                .type(BOOLEAN)
                                                .description("이해여부"),
                                        fieldWithPath("postDTO.userEmail")
                                                .type(STRING)
                                                .description("작성자 이메일"),
                                        fieldWithPath("postDTO.userId")
                                                .type(NUMBER)
                                                .description("작성자 ID"),
                                        fieldWithPath("postDTO.createdAt")
                                                .type(STRING)
                                                .description("작성일시")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{PUT} 포스트 업데이트 - 정상호출")
    @WithUserPrincipals
    @Test
    void updateKnowledge() throws Exception {
        JSONObject request = new JSONObject();
        request.put("title", "RESTful API 이해하기");
        request.put("content", "Restful API란.....222");
        request.put("link", "https://abcdefssss/2in2/update");

        when(postService.update(anyString(), any(Long.class), any(PostUpdateRequest.class)))
                .thenReturn(createKnowledgeUpdateResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/posts/1")
                                .with(csrf().asHeader())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "update-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("제목"),
                                        fieldWithPath("content").type(STRING).description("컨텐츠"),
                                        fieldWithPath("link").type(STRING).description("관련링크")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("지식 ID"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("컨텐츠"),
                                        fieldWithPath("link")
                                                .type(STRING)
                                                .description("관련링크"),
                                        fieldWithPath("isUnderstand")
                                                .type(BOOLEAN)
                                                .description("이해여부")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{DELETE} 포스트삭제 - 정상호출")
    @WithUserPrincipals
    @Test
    void deletePost() throws Exception {
        when(postService.delete(anyString(), any(Long.class)))
                .thenReturn("deleted");

        mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/posts/1")
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "delete-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private PostAddResponse createPostAddResponse() {
        return PostAddResponse.of(1L, "RESTful API", "Restful API란.....222", "https://abcdefssss/2in2", false);
    }

    private PostGetResponse createPostDetailGetResponse() {
        return PostGetResponse.builder()
                .userEmail("test@gmail.com")
                .content("Restful API란.....")
                .createdAt(LocalDateTime.now().toString())
                .build();
    }

    private PostUpdateResponse createKnowledgeUpdateResponse() {
        return PostUpdateResponse.builder()
                .id(1L)
                .title("RESTful API 이해하기")
                .content("Restful API란.....")
                .link("https://abcdefssss/2in2/update")
                .isUnderstand(false)
                .build();
    }
}