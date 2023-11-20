package com.l1mit.qma_server.domain.question.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.domain.question.service.QuestionService;
import com.l1mit.qma_server.global.common.domain.MBTI;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.setting.docs.RestDocsControllerTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
                    .mbti("ENTP")
                    .build();

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/question")
                    .content(toJson(questionRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isNoContent())
                    .andDo(document("question-create",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("질문")
                                            .summary("업로드")
                                            .description("질문을 업로드 하는 API")
                                            .requestFields(
                                                    fieldWithPath("content").description("질문 내용"),
                                                    fieldWithPath("mbti").description("MBTI")
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
        @DisplayName("글쓴이만 조회해서 성공한다")
        void success() throws Exception {
            //given
            int pageNumber = 0;
            int pageSize = 10;

            QuestionSearchParam questionSearchParam = QuestionSearchParam.builder()
                    .writer("글쓴이")
                    .build();

            Member member = getMember(getOauth2Entity("1", SocialProvider.KAKAO));
            member.updateMbtiEntity(getMbtiEntity("ENFP"));
            member.updateNickname("글쓴이");

            QuestionResponse questionResponse1 = getQuestionResponse(1L, "글쓴이", "ISTJ", LocalDateTime.of(2023, 11, 2, 7, 23, 2));
            QuestionResponse questionResponse2 = getQuestionResponse(2L, "글쓴이이", "ENTP", LocalDateTime.of(2023, 11, 3, 16, 14, 2));
            QuestionResponse questionResponse3 = getQuestionResponse(3L, "글글쓴이", "ISFP", LocalDateTime.of(2023, 11, 4, 2, 23, 2));

            List<QuestionResponse> questionResponses = List.of(questionResponse1, questionResponse2, questionResponse3);

            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

            given(questionService.searchWithCondition(pageRequest, questionSearchParam))
                    .willReturn(PageableExecutionUtils.getPage(questionResponses, pageRequest, questionResponses::size));
            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/question/search")
                    .queryParam("page", String.valueOf(pageRequest.getPageNumber()))
                    .queryParam("size", String.valueOf(pageRequest.getPageSize()))
                    .queryParam("writer", questionSearchParam.writer())
                    .queryParam("send_mbti", questionSearchParam.sendMbti())
                    .queryParam("receive_mbti", questionSearchParam.receiveMbti())
                    .queryParam("start_time", String.valueOf(questionSearchParam.startTime()))
                    .queryParam("end_time", String.valueOf(questionSearchParam.endTime()))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(document("question-search",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("질문")
                                            .summary("조회")
                                            .description("질문을 조건에 따라 검색 하는 API")
                                            .queryParameters(
                                                    parameterWithName("page").optional().type(SimpleType.NUMBER).defaultValue(0)
                                                            .description("페이지 번호"),
                                                    parameterWithName("size").optional().type(SimpleType.NUMBER).defaultValue(10)
                                                            .description("페이지 크기"),
                                                    parameterWithName("writer").optional().type(SimpleType.STRING)
                                                            .description("작성자"),
                                                    parameterWithName("send_mbti").optional().type(SimpleType.STRING)
                                                            .description("작성자 MBTI"),
                                                    parameterWithName("receive_mbti").optional().type(SimpleType.STRING)
                                                            .description("작성자 MBTI"),
                                                    parameterWithName("start_time").optional().type(SimpleType.STRING)
                                                            .description("검색 시작 날짜"),
                                                    parameterWithName("end_time").optional().type(SimpleType.STRING)
                                                            .description("검색 종료 날짜")
                                            )
                                            .responseFields(
                                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                            .description("응답 데이터"),
                                                    fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                                            .description("페이지 데이터"),
                                                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                                                            .description("질문 번호"),
                                                    fieldWithPath("data.content[].writer").type(JsonFieldType.STRING)
                                                            .description("작성자"),
                                                    fieldWithPath("data.content[].mbti").type(JsonFieldType.STRING)
                                                            .description("질문 대상 MBTI"),
                                                    fieldWithPath("data.content[].created_at").type(JsonFieldType.STRING)
                                                            .description("작성일"),
                                                    fieldWithPath("data.totalElement").type(JsonFieldType.NUMBER)
                                                            .description("전체 질문 개수"),
                                                    fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                                            .description("전체 페이지 수"),
                                                    fieldWithPath("message").type(JsonFieldType.NULL)
                                                            .description("오류 메세지"),
                                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                            .description("응답 시간")

                                            )
                                            .responseSchema(Schema.schema("QuestionResponse"))
                                            .build()
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("대상 MBTI만 조회해서 성공한다")
        void success_receiveMBTI() throws Exception {
            //given
            int pageNumber = 0;
            int pageSize = 10;

            QuestionSearchParam questionSearchParam = QuestionSearchParam.builder()
                    .writer("글쓴이")
                    .receiveMbti("ISTJ")
                    .build();

            Member member = getMember(getOauth2Entity("1", SocialProvider.KAKAO));
            member.updateMbtiEntity(getMbtiEntity("ENFP"));
            member.updateNickname("글쓴이");

            QuestionResponse questionResponse1 = getQuestionResponse(1L, "글쓴이", "ISTJ", LocalDateTime.of(2023, 11, 2, 7, 23, 2));
            QuestionResponse questionResponse2 = getQuestionResponse(2L, "글쓴이이", "ISTJ", LocalDateTime.of(2023, 11, 3, 16, 14, 2));
            QuestionResponse questionResponse3 = getQuestionResponse(3L, "글글쓴이", "ISTJ", LocalDateTime.of(2023, 11, 4, 2, 23, 2));
            List<QuestionResponse> questionResponses = List.of(questionResponse1, questionResponse2, questionResponse3);

            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
            given(questionService.searchWithCondition(any(Pageable.class), any(QuestionSearchParam.class)))
                    .willReturn(PageableExecutionUtils.getPage(questionResponses, pageRequest, questionResponses::size));
            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/question/search")
                    .queryParam("page", String.valueOf(pageRequest.getPageNumber()))
                    .queryParam("size", String.valueOf(pageRequest.getPageSize()))
                    .queryParam("writer", questionSearchParam.writer())
                    .queryParam("send_mbti", questionSearchParam.sendMbti())
                    .queryParam("send_mbti", questionSearchParam.sendMbti())
                    .queryParam("receive_mbti", questionSearchParam.receiveMbti())
                    .queryParam("start_time", String.valueOf(questionSearchParam.startTime()))
                    .queryParam("end_time", String.valueOf(questionSearchParam.endTime()))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(document("question-search",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("질문")
                                            .summary("조회")
                                            .description("질문을 조건에 따라 검색 하는 API")
                                            .queryParameters(
                                                    parameterWithName("page").optional().type(SimpleType.NUMBER).defaultValue(0)
                                                            .description("페이지 번호"),
                                                    parameterWithName("size").optional().type(SimpleType.NUMBER).defaultValue(10)
                                                            .description("페이지 크기"),
                                                    parameterWithName("writer").optional().type(SimpleType.STRING)
                                                            .description("작성자"),
                                                    parameterWithName("send_mbti").optional().type(SimpleType.STRING)
                                                            .description("작성자 MBTI"),
                                                    parameterWithName("receive_mbti").optional().type(SimpleType.STRING)
                                                            .description("작성자 MBTI"),
                                                    parameterWithName("start_time").optional().type(SimpleType.STRING)
                                                            .description("검색 시작 날짜"),
                                                    parameterWithName("end_time").optional().type(SimpleType.STRING)
                                                            .description("검색 종료 날짜")
                                            )
                                            .responseFields(
                                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                            .description("응답 데이터"),
                                                    fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                                            .description("페이지 데이터"),
                                                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                                                            .description("질문 번호"),
                                                    fieldWithPath("data.content[].writer").type(JsonFieldType.STRING)
                                                            .description("작성자"),
                                                    fieldWithPath("data.content[].mbti").type(JsonFieldType.STRING)
                                                            .description("질문 대상 MBTI"),
                                                    fieldWithPath("data.content[].created_at").type(JsonFieldType.STRING)
                                                            .description("작성일"),
                                                    fieldWithPath("data.totalElement").type(JsonFieldType.NUMBER)
                                                            .description("전체 질문 개수"),
                                                    fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                                            .description("전체 페이지 수"),
                                                    fieldWithPath("message").type(JsonFieldType.NULL)
                                                            .description("오류 메세지"),
                                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                            .description("응답 시간")

                                            )
                                            .responseSchema(Schema.schema("QuestionResponse"))
                                            .build()
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("searchDetail 메소드는")
    class searchDetail {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            Long questionId = 1L;
            QuestionDetailResponse questionDetailResponse = QuestionDetailResponse.builder()
                    .id(1L)
                    .writer("글쓴이")
                    .mbti(getMbti("ENTP"))
                    .content("특정 상황에 대한 질문")
                    .createdAt(LocalDate.of(2023, 11, 1).atStartOfDay())
                    .build();
            given(questionService.getDetail(questionId))
                    .willReturn(questionDetailResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/question/{id}", questionId)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(document("question-search-detail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .tag("질문")
                                    .summary("조회")
                                    .description("질문 번호에 맞는 질문을 조회하는 API")
                                    .pathParameters(
                                            parameterWithName("id").type(SimpleType.NUMBER)
                                                    .description("질문 번호")
                                    )
                                    .responseFields(
                                            fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                    .description("응답 데이터"),
                                            fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                    .description("질문 번호"),
                                            fieldWithPath("data.writer").type(JsonFieldType.STRING)
                                                    .description("질문자"),
                                            fieldWithPath("data.mbti").type(JsonFieldType.STRING)
                                                    .description("MBTI"),
                                            fieldWithPath("data.content").type(JsonFieldType.STRING)
                                                    .description("내용"),
                                            fieldWithPath("data.created_at").type(JsonFieldType.STRING)
                                                    .description("작성일"),
                                            fieldWithPath("message").type(JsonFieldType.NULL)
                                                    .description("오류 메세지"),
                                            fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                    .description("응답 시간")
                                    )
                                    .responseSchema(Schema.schema("QuestionDetailResponse"))
                                    .build()
                            )));

        }
    }

    @Nested
    @DisplayName("questionRemove 메소드는")
    class questionRemove {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            Long questionId = 1L;

            //when
            ResultActions resultActions = mockMvc.perform(delete("/api/v1/question/{id}", questionId)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isNoContent())
                    .andDo(document("question-delete",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("질문")
                                            .summary("삭제")
                                            .description("질문을 작성한 본인이 질문을 삭제하는 API")
                                            .pathParameters(
                                                    parameterWithName("id").type(SimpleType.NUMBER)
                                                            .description("질문 번호")
                                            )
                                            .responseFields(
                                                    fieldWithPath("data").type(JsonFieldType.NULL)
                                                            .description("응답 데이터"),
                                                    fieldWithPath("message").type(JsonFieldType.NULL)
                                                            .description("오류 메세지"),
                                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                            .description("응답 시간")
                                            )
                                            .build()
                                    )
                            )
                    );
        }
    }

    private Oauth2Entity getOauth2Entity(String accountId, SocialProvider socialProvider) {
        return Oauth2Entity.builder()
                .accountId(accountId)
                .socialProvider(socialProvider)
                .build();
    }

    private Member getMember(Oauth2Entity oauth2Entity) {
        return Member.builder()
                .oauth2Entity(oauth2Entity)
                .build();
    }

    private QuestionResponse getQuestionResponse(Long id, String writer, String mbti, LocalDateTime createdAt) {
        return QuestionResponse.builder()
                .id(id)
                .writer(writer)
                .mbti(MBTI.valueOf(mbti))
                .createdAt(createdAt)
                .build();
    }

    private MbtiEntity getMbtiEntity(String mbti) {
        return MbtiEntity.builder()
                .mbti(getMbti(mbti))
                .build();
    }

    @NotNull
    private static MBTI getMbti(String mbti) {
        return MBTI.valueOf(mbti);
    }

}