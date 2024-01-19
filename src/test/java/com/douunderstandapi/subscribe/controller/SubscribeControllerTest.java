package com.douunderstandapi.subscribe.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.annotation.WithUserPrincipals;
import com.douunderstandapi.post.dto.PostDTO;
import com.douunderstandapi.subscribe.dto.response.SubscribeAddResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribeCancelResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribePostsGetResponse;
import com.douunderstandapi.subscribe.service.SubscribeService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[컨트롤러] - 구독")
@WebMvcTest(SubscribeController.class)
@AutoConfigureRestDocs
class SubscribeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private SubscribeService subscribeService;

    @DisplayName("{GET} 구독 포스트 조회 - 정상호출")
    @WithUserPrincipals
    @Test
    void getSubscribePosts() throws Exception {
        when(subscribeService.getSubscribePosts(anyString(), anyInt()))
                .thenReturn(createSubscribePostsGetResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/subscribe")
                                .param("pageNumber", "5")
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "get-subscribe-posts",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("totalPageCount")
                                                .type(NUMBER)
                                                .description("페이지 카운트"),
                                        fieldWithPath("postList")
                                                .type(ARRAY)
                                                .description("포스트 정보"),
                                        fieldWithPath("postList[].id")
                                                .type(NUMBER)
                                                .description("포스트 ID"),
                                        fieldWithPath("postList[].title")
                                                .type(STRING)
                                                .description("제목"),
                                        fieldWithPath("postList[].content")
                                                .type(STRING)
                                                .description("컨텐츠"),
                                        fieldWithPath("postList[].link")
                                                .type(STRING)
                                                .description("관련링크"),
                                        fieldWithPath("postList[].commentCount")
                                                .type(NUMBER)
                                                .description("코멘트 카운트"),
                                        fieldWithPath("postList[].userEmail")
                                                .type(STRING)
                                                .description("이메일"),
                                        fieldWithPath("postList[].userId")
                                                .type(NUMBER)
                                                .description("유저 ID"),
                                        fieldWithPath("postList[].createdAt")
                                                .type(STRING)
                                                .description("작성일시"),
                                        fieldWithPath("postList[].subscribeMe")
                                                .type(BOOLEAN)
                                                .description("구독여부")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{POST} 포스트 구독 - 정상호출")
    @WithUserPrincipals
    @Test
    void addSubscribe() throws Exception {
        when(subscribeService.addSubscribe(anyString(), anyLong()))
                .thenReturn(new SubscribeAddResponse(true));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/subscribe/{postId}", 1)
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "add-subscribe-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("isAdded")
                                                .type(BOOLEAN)
                                                .description("구독 여부")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("{POST} 포스트 구독 취소 - 정상호출")
    @WithUserPrincipals
    @Test
    void cancelSubscribe() throws Exception {
        when(subscribeService.cancelSubscribe(anyString(), anyLong()))
                .thenReturn(new SubscribeCancelResponse(true));

        mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/subscribe/{postId}", 1)
                                .with(csrf().asHeader()))
                .andDo(print())
                .andDo(
                        document(
                                "cancel-subscribe-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("isCanceled")
                                                .type(BOOLEAN)
                                                .description("구독 여부")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    private SubscribePostsGetResponse createSubscribePostsGetResponse() {
        List<PostDTO> postDTOS = new ArrayList<>();
        String title = "RESTful API";
        String content = "Restful API란.....";
        String link = "https://abcdefssss/2in2";
        for (int i = 1; i < 3; i++) {
            PostDTO postDTO = createPostDTO(
                    (long) i,
                    title + ":" + i,
                    content + ":" + i,
                    link
            );
            postDTOS.add(postDTO);
        }
        return new SubscribePostsGetResponse(5, postDTOS);
    }

    private PostDTO createPostDTO(Long id, String title, String content, String link) {
        return PostDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .link(link)
                .commentCount(0L)
                .userEmail("test@gmail.com")
                .userId(1L)
                .createdAt(LocalDateTime.now().toString())
                .subscribeMe(true)
                .build();
    }
}