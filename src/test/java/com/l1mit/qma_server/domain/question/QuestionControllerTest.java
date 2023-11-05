package com.l1mit.qma_server.domain.question;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.l1mit.qma_server.domain.question.dto.QuestionRequest;
import com.l1mit.qma_server.setting.docs.RestDocsControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = QuestionController.class)
class QuestionControllerTest extends RestDocsControllerTest {

    @MockBean
    QuestionService questionService;

    @Nested
    @DisplayName("insert 메소드는")
    class insert {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            QuestionRequest questionRequest = QuestionRequest.builder()
                    .content("질문 내용")
                    .attitude("E")
                    .perception("N")
                    .lifestyle("F")
                    .decision("P")
                    .build();

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/question")
                    .content(toJson(questionRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isNoContent())
                    .andDo(document("insert",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("질문")
                                            .summary("업로드")
                                            .description("질문을 업로드 하는 API")
                                            .requestFields(
                                                    fieldWithPath("content").description("질문 내용"),
                                                    fieldWithPath("attitude").description("E, I"),
                                                    fieldWithPath("perception").description("N, S"),
                                                    fieldWithPath("decision").description("T, F"),
                                                    fieldWithPath("lifestyle").description("P, J")
                                            )
                                            .responseFields(
                                                    fieldWithPath("data").description("응답 데이터"),
                                                    fieldWithPath("message").description("오류 메세지"),
                                                    fieldWithPath("timestamp").description("응답 시간")
                                            )
                                            .requestSchema(Schema.schema("QuestionRequest"))
                                            .build())
                            )
                    );
        }

    }
}