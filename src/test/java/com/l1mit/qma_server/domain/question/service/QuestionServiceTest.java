package com.l1mit.qma_server.domain.question.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.request.QuestionRequest;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.domain.question.repository.QuestionRepository;
import com.l1mit.qma_server.global.common.domain.MBTI;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import com.l1mit.qma_server.global.facade.QuestionFacade;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    QuestionService questionService;

    @Mock
    QuestionFacade questionFacade;

    @Mock
    QuestionRepository questionRepository;

    @Nested
    @DisplayName("save 메소드는")
    class save {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            Long memberId = 1L;
            QuestionRequest questionRequest = QuestionRequest.builder()
                    .content("질문 내용")
                    .mbti("ENTP")
                    .build();

            Oauth2Entity oauth2Entity = getOauth2Entity("123", SocialProvider.KAKAO);
            Member member = getMember(oauth2Entity);
            MbtiEntity mbtiEntity = getMbtiEntity("ENTP");

            Question question = getQuestion(member, mbtiEntity, "질문 내용");

            given(questionFacade.create(questionRequest, memberId))
                    .willReturn(question);
            given(questionRepository.save(question))
                    .willReturn(question);

            //when
            Question result = questionService.save(memberId, questionRequest);

            //then
            assertThat(result).isEqualTo(question);

        }
    }

    @Nested
    @DisplayName("searchWithCondition 메소드는")
    class searchWithCondition {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            PageRequest pageRequest = PageRequest.of(0, 10);
            QuestionSearchParam param = QuestionSearchParam.builder()
                    .build();
            QuestionResponse questionResponse1 = getQuestionResponse(
                    1L, "질문자", "ENTP",
                    LocalDateTime.of(2023, 11, 1, 1, 14, 23));
            QuestionResponse questionResponse2 = getQuestionResponse(
                    2L, "질문자", "ENTP",
                    LocalDateTime.of(2023, 11, 1, 1, 14, 23));
            QuestionResponse questionResponse3 = getQuestionResponse(
                    3L, "질문자", "ENTP",
                    LocalDateTime.of(2023, 11, 1, 1, 14, 23));

            List<QuestionResponse> responseList = List.of(questionResponse1,
                    questionResponse2, questionResponse3);

            given(questionRepository.searchWithCondition(pageRequest, param))
                    .willReturn(new PageImpl<>(
                            responseList,
                            pageRequest,
                            responseList.size()));

            //when
            Page<QuestionResponse> result = questionService.searchWithCondition(
                    pageRequest, param);

            //then
            assertThat(result.getContent()).isEqualTo(responseList);
            assertThat(result.getNumberOfElements()).isEqualTo(responseList.size());
            assertThat(result.getTotalPages()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("getDetail 메소드는")
    class getDetail {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            Long questionId = 1L;
            QuestionDetailResponse questionDetailResponse = QuestionDetailResponse.builder()
                    .writer("질문자")
                    .mbti(MBTI.ENTP)
                    /*.attitude("E")
                    .perception("N")
                    .decision("T")
                    .lifestyle("P")*/
                    .content("질문 내용")
                    .createdAt(LocalDateTime.of(2023, 11, 1, 1, 14, 23))
                    .build();

            given(questionRepository.findByIdWithDetail(questionId))
                    .willReturn(Optional.ofNullable(questionDetailResponse));
            //when
            QuestionDetailResponse result = questionService.getDetail(questionId);

            //then
            assertThat(result).isEqualTo(questionDetailResponse);
        }

        @Test
        @DisplayName("존재하지 않는 질문이면 예외를 발생시킨다.")
        void fail_Null() {
            //given
            Long questionId = 1L;

            //when

            //then
            assertThatThrownBy(() ->
                    questionService.getDetail(questionId))
                    .isInstanceOf(QmaApiException.class)
                    .hasMessageContaining(ErrorCode.NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class findById {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            Long questionId = 1L;
            Question question = getQuestion(
                    getMember(getOauth2Entity("123", SocialProvider.KAKAO))
                    , getMbtiEntity("ENTP")
                    , "질문 내용");
            given(questionRepository.findById(questionId))
                    .willReturn(Optional.ofNullable(question));

            //when
            Question result = questionService.findById(questionId);

            //then
            assertThat(result).isEqualTo(question);
        }

        @Test
        @DisplayName("존재하지 않는 질문이면 예외를 발생시킨다.")
        void fail_Null() {
            //given
            Long questionId = 1L;

            //when

            //then
            assertThatThrownBy(() ->
                    questionService.findById(questionId))
                    .isInstanceOf(QmaApiException.class)
                    .hasMessageContaining(ErrorCode.NOT_FOUND.getMessage());
        }
    }

    private Oauth2Entity getOauth2Entity(String accountId, SocialProvider socialProvider) {
        Oauth2Entity oauth2Entity = Oauth2Entity.builder()
                .accountId(accountId)
                .socialProvider(socialProvider)
                .build();
        return oauth2Entity;
    }

    private Member getMember(Oauth2Entity oauth2Entity) {
        Member member = Member.builder()
                .oauth2Entity(oauth2Entity)
                .build();
        return member;
    }

    private MbtiEntity getMbtiEntity(
            String mbti) {
        MbtiEntity mbtiEntity = MbtiEntity.builder()
                .mbti(MBTI.valueOf(mbti))
                .build();
        return mbtiEntity;
    }

    private Question getQuestion(Member member, MbtiEntity mbtiEntity, String content) {
        return Question.builder()
                .content(content)
                .member(member)
                .receiveMbtiEntity(mbtiEntity)
                .build();
    }

    private QuestionResponse getQuestionResponse(
            Long id,
            String writer,
            String mbti,
            LocalDateTime createdAt) {
        return QuestionResponse.builder()
                .id(id)
                .writer(writer)
                .mbti(MBTI.valueOf(mbti))
                /*.attitude(attitude)
                .perception(perception)
                .decision(decision)
                .lifestyle(lifestyle)*/
//                .createdAt(createdAt)
                .build();
    }
}