package com.l1mit.qma_server.domain.question;

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
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.question.dto.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.QuestionResponse;
import com.l1mit.qma_server.domain.question.dto.QuestionSearchParam;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.setting.docs.RestDocsControllerTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
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

    @Nested
    @DisplayName("search 메소드는")
    class search {

        @Test
        @DisplayName("성공한다")
        void success() throws Exception {
            //given
            QuestionSearchParam questionSearchParam = QuestionSearchParam.builder()
                    .writer("때지")
                    .sendAttitude("E")
                    .sendPerception("N")
                    .sendDecision("F")
                    .sendLifestyle("P")
                    .receiveAttitude("I")
                    .receivePerception("S")
                    .receiveDecision("T")
                    .receiveLifestyle("J")
                    .startTime(LocalDate.of(2023, 11, 1))
                    .endTime(LocalDate.of(2023, 11, 3))
                    .build();

            Oauth2Entity oauth2Entity = Oauth2Entity.builder()
                    .accountId("1")
                    .socialProvider(SocialProvider.KAKAO)
                    .build();

            Member member = Member.builder()
                    .oauth2Entity(oauth2Entity)
                    .build();

            member.updateNickname("때지");

            Question question = Question.builder()
                    .member(member)
                    .content("특정 상황에 대한 질문")
                    .receiveMbtiEntity(
                            MbtiEntity.builder()
                                    .attitude("I")
                                    .perception("S")
                                    .decision("T")
                                    .lifestyle("J")
                                    .build()
                    )
                    .build();

            QuestionResponse questionResponse = QuestionResponse.builder()
                    .writer("때지")
                    .attitude("I")
                    .perception("S")
                    .decision("T")
                    .lifestyle("J")
                    .createdAt(LocalDate.of(2023, 11, 2))
                    .build();

            PageRequest pageRequest = PageRequest.of(0, 10);

            given(questionService.searchWithCondition(pageRequest, questionSearchParam))
                    .willReturn(List.of(questionResponse));
            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/question/search")
                    .queryParam("page", String.valueOf(pageRequest.getPageNumber()))
                    .queryParam("size", String.valueOf(pageRequest.getPageSize()))
                    .queryParam("writer", questionSearchParam.writer())
                    .queryParam("sendAttitude", questionSearchParam.sendAttitude())
                    .queryParam("sendPerception", questionSearchParam.sendPerception())
                    .queryParam("sendDecision", questionSearchParam.sendDecision())
                    .queryParam("sendLifestyle", questionSearchParam.sendLifestyle())
                    .queryParam("receiveAttitude", questionSearchParam.receiveAttitude())
                    .queryParam("receivePerception", questionSearchParam.receivePerception())
                    .queryParam("receiveDecision", questionSearchParam.receiveDecision())
                    .queryParam("receiveLifestyle", questionSearchParam.receiveLifestyle())
                    .queryParam("startTime", questionSearchParam.startTime().toString())
                    .queryParam("endTime", questionSearchParam.endTime().toString())
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(document("create",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("질문")
                                            .summary("조회")
                                            .description("질문을 조건에 따라 검색 하는 API")
                                            .queryParameters(
                                                    parameterWithName("page").optional()
                                                            .description("페이지 번호"),
                                                    parameterWithName("size").optional()
                                                            .description("페이지 크기"),
                                                    parameterWithName("writer").optional()
                                                            .description("작성자"),
                                                    parameterWithName("sendAttitude").optional()
                                                            .description("보낸이 E, I"),
                                                    parameterWithName("sendPerception").optional()
                                                            .description("보낸이 N, S"),
                                                    parameterWithName("sendDecision").optional()
                                                            .description("보낸이 T, F"),
                                                    parameterWithName("sendLifestyle").optional()
                                                            .description("보낸이 P, J"),
                                                    parameterWithName("receiveAttitude").optional()
                                                            .description("대상 E, I"),
                                                    parameterWithName("receivePerception").optional()
                                                            .description("대상 N, S"),
                                                    parameterWithName("receiveDecision").optional()
                                                            .description("대상 T, F"),
                                                    parameterWithName("receiveLifestyle").optional()
                                                            .description("대상 P, J"),
                                                    parameterWithName("startTime").optional()
                                                            .description("검색 시작 날짜"),
                                                    parameterWithName("endTime").optional()
                                                            .description("검색 종료 날짜")
                                            )
                                            .responseFields(
                                                    fieldWithPath("data").description("응답 데이터"),
                                                    fieldWithPath("data[].writer").description("작성자"),
                                                    fieldWithPath("data[].attitude").description("대상 E, I"),
                                                    fieldWithPath("data[].perception").description(
                                                            "대상 N, S"),
                                                    fieldWithPath("data[].decision").description("대상 T, F"),
                                                    fieldWithPath("data[].lifestyle").description(
                                                            "대상 P, J"),
                                                    fieldWithPath("data[].created_at").description("작성일"),
                                                    fieldWithPath("message").description("오류 메세지"),
                                                    fieldWithPath("timestamp").description("응답 시간")

                                            )
                                            .responseSchema(Schema.schema("QuestionResponse"))
                                            .build()
                                    )
                            )
                    );
        }
    }
}