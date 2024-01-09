package com.douunderstandapi.knowledge.controller;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.annotation.WithUserPrincipals;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeSubscribeUpdateResponse;
import com.douunderstandapi.knowledge.service.KnowledgeSubscribeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[컨트롤러] - 지식구독")
@WebMvcTest(KnowledgeSubscribeController.class)
@AutoConfigureRestDocs
class KnowledgeSubscribeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private KnowledgeSubscribeService knowledgeSubscribeService;

    @DisplayName("{PATCH} 지식구독 업데이트 - 정상호출")
    @WithUserPrincipals
    @Test
    void updateKnowledgeSubscribe() throws Exception {
        when(knowledgeSubscribeService.updateKnowledgeSubscribe(anyString(), any(Long.class), any(Boolean.class)))
                .thenReturn(createKnowledgeSubscribeUpdateResponseResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.patch("/api/v1/knowledge/1")
                                .with(csrf().asHeader())
                                .param("isSubscribe", "true"))
                .andDo(print())
                .andDo(
                        document(
                                "update-subscribe-knowledge",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
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
                                                .description("이해여부"),
                                        fieldWithPath("isSubscribe")
                                                .type(BOOLEAN)
                                                .description("구독여부")
                                )

                        )
                )
                .andExpect(status().isOk());
    }

    private KnowledgeSubscribeUpdateResponse createKnowledgeSubscribeUpdateResponseResponse() {
        return KnowledgeSubscribeUpdateResponse.of(1L, "RESTful API 이해하기", "Restful API란.....",
                "https://abcdefssss/2in2/update",
                false, true);
    }
}