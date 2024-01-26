package com.douunderstandapi.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.annotation.WithUserPrincipals;
import com.douunderstandapi.comment.dto.CommentDTO;
import com.douunderstandapi.comment.dto.request.CommentAddRequest;
import com.douunderstandapi.comment.dto.request.CommentDeleteRequest;
import com.douunderstandapi.comment.dto.response.CommentAddResponse;
import com.douunderstandapi.comment.dto.response.CommentDeleteResponse;
import com.douunderstandapi.comment.dto.response.CommentsGetResponse;
import com.douunderstandapi.comment.service.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.LongStream;
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

@DisplayName("[컨트롤러] - 코멘트")
@WebMvcTest(CommentController.class)
@AutoConfigureRestDocs
class CommentControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @DisplayName("{POST} 코멘트 목록 조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getComments() throws Exception {
        Long postId = 1L;
        when(commentService.getComments(anyString(), anyLong()))
                .thenReturn(createCommentsGetResponse(postId));
        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/comments/{postId}", 1)
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString()))
                .andDo(print())
                .andDo(
                        document(
                                "get-comments",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("comments")
                                                .type(ARRAY)
                                                .description("코멘트 정보 목록"),
                                        fieldWithPath("comments[].id")
                                                .type(NUMBER)
                                                .description("코멘트 ID"),
                                        fieldWithPath("comments[].postId")
                                                .type(NUMBER)
                                                .description("포스트 ID"),
                                        fieldWithPath("comments[].content")
                                                .type(STRING)
                                                .description("코멘트 컨텐츠"),
                                        fieldWithPath("comments[].createdAt")
                                                .type(STRING)
                                                .description("코멘트 작성일시"),
                                        fieldWithPath("comments[].userId")
                                                .type(NUMBER)
                                                .description("코멘트 작성유저 ID"),
                                        fieldWithPath("comments[].userEmail")
                                                .type(STRING)
                                                .description("코멘트 작성유저 이메일")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    private CommentsGetResponse createCommentsGetResponse(Long postId) {
        String createdAt = LocalDateTime.now().toString();
        List<CommentDTO> commentDTOS = LongStream
                .range(1, 3)
                .mapToObj(i -> CommentDTO.builder()
                        .id(i)
                        .postId(postId)
                        .userId(i)
                        .content("좋은 내용입니다. " + i)
                        .createdAt(createdAt)
                        .userEmail("selfnews" + i + "@gmail.com")
                        .build())
                .toList();
        return new CommentsGetResponse(commentDTOS);
    }

    @DisplayName("{POST} 코멘트 등록 - 정상호출")
    @WithUserPrincipals
    @Test
    void addComment() throws Exception {
        JSONObject request = new JSONObject();
        Long postId = 1L;
        String content = "좋은 내용입니다!";
        request.put("postId", postId);
        request.put("content", content);

        when(commentService.addComment(anyString(), any(CommentAddRequest.class)))
                .thenReturn(createCommentAddResponse(content, postId));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/comments")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "add-comment",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("postId").type(NUMBER).description("포스트 ID"),
                                        fieldWithPath("content").type(STRING).description("컨텐츠")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("코멘트 ID"),
                                        fieldWithPath("postId")
                                                .type(NUMBER)
                                                .description("포스트 ID"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("코멘트 컨텐츠"),
                                        fieldWithPath("createdAt")
                                                .type(STRING)
                                                .description("코멘트 작성일시"),
                                        fieldWithPath("userId")
                                                .type(NUMBER)
                                                .description("코멘트 작성유저 ID"),
                                        fieldWithPath("userEmail")
                                                .type(STRING)
                                                .description("코멘트 작성유저 이메일")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    private CommentAddResponse createCommentAddResponse(String content, Long postId) {
        return CommentAddResponse.builder()
                .id(1L)
                .postId(postId)
                .content(content)
                .createdAt(LocalDateTime.now().toString())
                .userId(1L)
                .userEmail("selfnews@gmail.com")
                .build();
    }

    @DisplayName("{POST} 코멘트 삭제 - 정상호출")
    @WithUserPrincipals
    @Test
    void deleteComment() throws Exception {
        Long commentId = 1L;
        JSONObject request = new JSONObject();
        request.put("commentId", commentId);
        when(commentService.deleteComment(anyString(), any(CommentDeleteRequest.class)))
                .thenReturn(createCommentDeleteResponse(commentId));

        mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/comments")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "delete-comment",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("commentId").type(NUMBER).description("코멘트 ID")
                                ),
                                responseFields(
                                        fieldWithPath("commentId")
                                                .type(NUMBER)
                                                .description("삭제된 코멘트 ID"))
                        )
                )
                .andExpect(status().isOk());
    }

    private CommentDeleteResponse createCommentDeleteResponse(Long commentId) {
        return new CommentDeleteResponse(commentId);
    }
}