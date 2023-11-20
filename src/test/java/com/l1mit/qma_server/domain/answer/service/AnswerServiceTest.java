package com.l1mit.qma_server.domain.answer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.l1mit.qma_server.domain.answer.domain.Answer;
import com.l1mit.qma_server.domain.answer.dto.request.AnswerRequest;
import com.l1mit.qma_server.domain.answer.dto.response.AnswerResponse;
import com.l1mit.qma_server.domain.answer.repository.AnswerRepository;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.global.common.domain.MBTI;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import com.l1mit.qma_server.global.facade.AnswerFacade;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @InjectMocks
    AnswerService answerService;
    @Mock
    AnswerRepository answerRepository;
    @Mock
    AnswerFacade answerFacade;

    @Nested
    @DisplayName("create 메소드는")
    class create {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            Long memberId = 1L;
            AnswerRequest answerRequest = AnswerRequest.builder()
                    .content("질문 내용")
                    .questionId(1L)
                    .build();
            Member member = getMember(
                    getOauth2Entity("123", SocialProvider.KAKAO));
            Answer answer = getAnswer(
                    member,
                    getQuestion(member,
                            getMbtiEntity("ENTP"),
                            "질문 내용"
                    ));
            given(answerFacade.create(answerRequest, memberId))
                    .willReturn(answer);

            //when
            answerService.create(answerRequest, memberId);

            //then
            verify(answerFacade, times(1)).create(answerRequest, memberId);
            verify(answerRepository, times(1)).save(answer);
        }
    }

    @Nested
    @DisplayName("findPagedAnswerByQuestionId 메소드는")
    class findPagedAnswerByQuestionId {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            int pageNumber = 0;
            int pageSize = 10;
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
            Long questionId = 1L;
            AnswerResponse answerResponse1 = getAnswerResponse("대답 내용1", "작성자1",
                    LocalDateTime.of(2023, 11, 1, 17, 2));
            AnswerResponse answerResponse2 = getAnswerResponse("대답 내용2", "작성자2",
                    LocalDateTime.of(2023, 11, 1, 17, 2));
            AnswerResponse answerResponse3 = getAnswerResponse("대답 내용3", "작성자3",
                    LocalDateTime.of(2023, 11, 1, 17, 2));
            List<AnswerResponse> answerResponses = List.of(answerResponse1, answerResponse2,
                    answerResponse3);

            PageImpl<AnswerResponse> answerResponsePage = new PageImpl<>(answerResponses,
                    pageRequest,
                    answerResponses.size());

            given(answerRepository.findPagedAnswerByQuestionId(pageRequest, questionId))
                    .willReturn(
                            answerResponsePage);

            //when
            Page<AnswerResponse> result = answerService.findPagedAnswerByQuestionId(
                    pageRequest, questionId);

            //then
            assertThat(result.getContent()).isEqualTo(answerResponses);
            assertThat(result.getContent().size()).isEqualTo(answerResponses.size());
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getNumberOfElements()).isEqualTo(answerResponses.size());
        }
    }

    @Nested
    @DisplayName("deleteById 메소드는")
    class deleteById {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            Member member = getMember(getOauth2Entity("123", SocialProvider.KAKAO));
            Member respondent = getMember(getOauth2Entity("123", SocialProvider.KAKAO));
            Question question = getQuestion(
                    member,
                    getMbtiEntity("ENTP"),
                    "질문 내용");
            Answer answer = getAnswer(
                    respondent,
                    question
                    );

            Long mockAnswerId = 1L;
            Long mockRespondentId = 1L;

            ReflectionTestUtils.setField(respondent, "id", mockRespondentId);
            ReflectionTestUtils.setField(answer, "id", mockAnswerId);


            given(answerRepository.findById(mockAnswerId))
                    .willReturn(Optional.of(answer));

            //when
            answerService.deleteById(mockAnswerId, mockRespondentId);

            //then
            verify(answerRepository, times(1)).deleteById(mockAnswerId);
        }

        @Test
        @DisplayName("본인이 작성한 대답이 아니라면 예외처리를 한다.")
        void fail_NotMatchingId() {
            //given
            Member writer = getMember(getOauth2Entity("123", SocialProvider.KAKAO));
            Member respondent = getMember(getOauth2Entity("132", SocialProvider.KAKAO));

            respondent.updateMbtiEntity(getMbtiEntity("ENTP"));

            Question question = getQuestion(
                    writer,
                    getMbtiEntity("ENTP"),
                    "질문 내용");

            Answer answer = getAnswer(
                    writer,
                    question
            );

            Long mockAnswerId = 1L;
            Long mockWriterId = 1L;
            Long mockRespondentId = 2L;

            ReflectionTestUtils.setField(answer, "id", mockAnswerId);
            ReflectionTestUtils.setField(respondent, "id", mockRespondentId);
            ReflectionTestUtils.setField(writer, "id", mockWriterId);

            given(answerRepository.findById(mockAnswerId))
                    .willReturn(Optional.of(answer));

            //when

            //then
            assertThatThrownBy(()->
                    answerService.deleteById(mockAnswerId, mockRespondentId))
                    .isInstanceOf(QmaApiException.class)
                    .hasMessageContaining(ErrorCode.NOT_RESPONDENT.getMessage());
        }
    }

    private AnswerResponse getAnswerResponse(String content, String writer,
            LocalDateTime createdAt) {
        return AnswerResponse.builder()
                .content(content)
                .writer(writer)
                .createdAt(createdAt)
                .build();
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

    private MbtiEntity getMbtiEntity(String mbti) {
        return MbtiEntity.builder()
                .mbti(MBTI.valueOf(mbti))
                .build();
    }

    private Question getQuestion(Member member, MbtiEntity mbtiEntity, String content) {
        return Question.builder()
                .content(content)
                .member(member)
                .receiveMbtiEntity(mbtiEntity)
                .build();
    }

    private Answer getAnswer(Member member, Question question) {
        return Answer.builder()
                .content("대답 내용")
                .member(member)
                .question(question)
                .build();
    }
}