package com.l1mit.qma_server.domain.answer.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.l1mit.qma_server.domain.answer.dto.request.AnswerRequest;
import com.l1mit.qma_server.domain.answer.dto.response.AnswerResponse;
import com.l1mit.qma_server.domain.answer.service.AnswerService;
import com.l1mit.qma_server.setting.docs.RestDocsControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = AnswerController.class)
class AnswerControllerTest extends RestDocsControllerTest {

    @MockBean
    AnswerService answerService;

    @Nested
    @DisplayName("create 메소드는")
    class create {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            Long memberId = 1L;
            AnswerRequest answerRequest = AnswerRequest.builder()
                    .content("대답 내용")
                    .questionId(memberId)
                    .build();
            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/answer")
                    .content(toJson(answerRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isNoContent())
                    .andDo(document("answer-create",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .tag("대답")
                                    .summary("업로드")
                                    .requestFields(
                                            fieldWithPath("content").type(JsonFieldType.STRING)
                                                    .description("대답 내용"),
                                            fieldWithPath("question_id").type(JsonFieldType.NUMBER)
                                                    .description("질문 번호")
                                    )
                                    .requestSchema(Schema.schema("AnswerRequest"))
                                    .build())
                    ));
        }
    }

    @Nested
    @DisplayName("getPagedAnswers 메소드는")
    class getPagedAnswers {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            int pageNumber = 0;
            int pageSize = 10;
            Long questionId = 1L;
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
            AnswerResponse answerResponse1 = AnswerResponse.builder()
                    .writer("글쓴이1")
                    .content("대답 내용")
                    .createdAt(LocalDateTime.of(2023, 11, 01, 13, 14))
                    .build();
            AnswerResponse answerResponse2 = AnswerResponse.builder()
                    .writer("글쓴이2")
                    .content("대답 내용")
                    .createdAt(LocalDateTime.of(2023, 11, 02, 05, 11))
                    .build();
            given(answerService.findPagedAnswerByQuestionId(pageRequest, questionId))
                    .willReturn(
                            new PageImpl<>(
                                    List.of(answerResponse1, answerResponse2),
                                    pageRequest,
                                    2)
                    );

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/answer")
                    .queryParam("id", "1")
                    .queryParam("size", "10")
                    .queryParam("page", "0")
                    .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(document("answer-search",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .tag("대답")
                                    .summary("페이지 조회")
                                    .queryParameters(
                                            parameterWithName("id").type(SimpleType.NUMBER)
                                                    .description("질문 번호"),
                                            parameterWithName("size").type(SimpleType.NUMBER)
                                                    .optional()
                                                    .description("페이지 크기"),
                                            parameterWithName("page").type(SimpleType.NUMBER)
                                                    .optional()
                                                    .description("페이지 번호")
                                    )
                                    .responseFields(
                                            fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                    .description("응답 데이터"),
                                            fieldWithPath("data.content[]").type(
                                                            JsonFieldType.ARRAY)
                                                    .description("페이지 데이터"),
                                            fieldWithPath("data.content[].writer").type(
                                                            JsonFieldType.STRING)
                                                    .description("댓글 쓴 사람"),
                                            fieldWithPath("data.content[].content").type(
                                                            JsonFieldType.STRING)
                                                    .description("댓글 내용"),
                                            fieldWithPath("data.content[].created_at").type(
                                                            JsonFieldType.STRING)
                                                    .description("댓글 작성일"),
                                            fieldWithPath("data.totalElement").type(
                                                            JsonFieldType.NUMBER)
                                                    .description("전체 댓글 개수"),
                                            fieldWithPath("data.totalPage").type(
                                                            JsonFieldType.NUMBER)
                                                    .description("전체 댓글 페이지 수"),
                                            fieldWithPath("message").type(JsonFieldType.NULL)
                                                    .description("에러 메세지"),
                                            fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                    .description("응답 시간")
                                    )
                                    .responseSchema(Schema.schema("AnswerResponse"))
                                    .build())
                    ));
        }

    }
}