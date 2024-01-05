package com.douunderstandapi.knowledge.controller;

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

import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeAddRequest;
import com.douunderstandapi.knowledge.domain.dto.request.KnowledgeUpdateRequest;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeAddResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeGetResponse;
import com.douunderstandapi.knowledge.domain.dto.response.KnowledgeUpdateResponse;
import com.douunderstandapi.knowledge.service.KnowledgeService;
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

@DisplayName("[컨트롤러] - 지식")
@WebMvcTest(KnowledgeController.class)
@AutoConfigureRestDocs
class KnowledgeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private KnowledgeService knowledgeService;

    @DisplayName("{POST} 지식등록 - 정상호출")
    @Test
    void addKnowledge() throws Exception {
        JSONObject request = new JSONObject();
        request.put("title", "RESTful API");
        request.put("content", "Restful API란.....");
        request.put("link", "https://abcdefssss/2in2");

        when(knowledgeService.addKnowledge(any(KnowledgeAddRequest.class)))
                .thenReturn(createKnowledgeAddResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/knowledge")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "create-knowledge",
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

    @DisplayName("{GET} 지식조회(상세) - 정상호출")
    @Test
    void getKnowledge() throws Exception {
        when(knowledgeService.findKnowledge(any(Long.class)))
                .thenReturn(createKnowledgeGetResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/knowledge/1")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andDo(
                        document(
                                "get-knowledge",
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
                                                .description("이해여부")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{PUT} 지식 업데이트 - 정상호출")
    @Test
    void updateKnowledge() throws Exception {
        JSONObject request = new JSONObject();
        request.put("title", "RESTful API 이해하기");
        request.put("content", "Restful API란.....222");
        request.put("link", "https://abcdefssss/2in2/update");
        request.put("email", "test@gmail.com");

        when(knowledgeService.update(any(Long.class), any(KnowledgeUpdateRequest.class)))
                .thenReturn(createKnowledgeUpdateResponse());

        mvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/knowledge/1")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "update-knowledge",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("제목"),
                                        fieldWithPath("content").type(STRING).description("컨텐츠"),
                                        fieldWithPath("link").type(STRING).description("관련링크"),
                                        fieldWithPath("email").type(STRING).description("유저 이메일")
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

    @DisplayName("{DELETE} 지식삭제 - 정상호출")
    @Test
    void deleteKnowledge() throws Exception {
        when(knowledgeService.delete(any(Long.class)))
                .thenReturn("deleted");

        mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/knowledge/1"))
                .andDo(print())
                .andDo(
                        document(
                                "delete-knowledge",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private KnowledgeAddResponse createKnowledgeAddResponse() {
        return KnowledgeAddResponse.of(1L, "RESTful API", "Restful API란.....222", "https://abcdefssss/2in2", false);
    }

    private KnowledgeGetResponse createKnowledgeGetResponse() {
        return KnowledgeGetResponse.of(1L, "RESTful API", "Restful API란.....", "https://abcdefssss/2in2", false);
    }

    private KnowledgeUpdateResponse createKnowledgeUpdateResponse() {
        return KnowledgeUpdateResponse.of(1L, "RESTful API 이해하기", "Restful API란.....", "https://abcdefssss/2in2/update",
                false);
    }
}