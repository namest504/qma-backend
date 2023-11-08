package com.l1mit.qma_server.global.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.l1mit.qma_server.domain.answer.Answer;
import com.l1mit.qma_server.domain.answer.AnswerMapper;
import com.l1mit.qma_server.domain.answer.dto.AnswerRequest;
import com.l1mit.qma_server.domain.member.MemberService;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.question.Question;
import com.l1mit.qma_server.domain.question.QuestionService;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnswerFacadeTest {

    @InjectMocks
    AnswerFacadeImpl answerFacade;
    @Mock
    MemberService memberService;
    @Mock
    QuestionService questionService;
    @Mock
    AnswerMapper answerMapper;

    @Nested
    @DisplayName("create 메소드는")
    class create {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            AnswerRequest answerRequest = AnswerRequest.builder()
                    .content("대답 내용")
                    .questionId(1L)
                    .build();
            MbtiEntity mbtiEntity = MbtiEntity.builder()
                    .attitude("E")
                    .perception("N")
                    .decision("T")
                    .lifestyle("P")
                    .build();
            Member member = Member.builder()
                    .oauth2Entity(
                            Oauth2Entity.builder()
                                    .accountId("123")
                                    .socialProvider(SocialProvider.KAKAO)
                                    .build()
                    )
                    .build();
            member.updateMbtiEntity(mbtiEntity);
            Question question = Question.builder()
                    .member(member)
                    .content("질문 내용")
                    .receiveMbtiEntity(mbtiEntity)
                    .build();
            Answer answer = Answer.builder()
                    .content("응답 내용")
                    .member(member)
                    .question(question)
                    .build();
            given(memberService.findById(anyLong()))
                    .willReturn(member);
            given(questionService.findById(anyLong()))
                    .willReturn(question);
            given(answerMapper.answerRequestToEntity(answerRequest, question, member))
                    .willReturn(answer);

            //when
            Answer result = answerFacade.create(answerRequest, 1L);

            //then
            assertThat(result).isEqualTo(answer);
        }

        @Test
        @DisplayName("질문의 대상 MBTI와 대답한 이의 MBTI가 일치하지 않아서 실패한다.")
        void fail_NotMatchingMbti() throws Exception {
            //given
            AnswerRequest answerRequest = AnswerRequest.builder()
                    .content("대답 내용")
                    .questionId(1L)
                    .build();
            Member member = Member.builder()
                    .oauth2Entity(
                            Oauth2Entity.builder()
                                    .accountId("123")
                                    .socialProvider(SocialProvider.KAKAO)
                                    .build()
                    )
                    .build();
            member.updateMbtiEntity(
                    MbtiEntity.builder()
                            .attitude("E")
                            .perception("N")
                            .decision("T")
                            .lifestyle("P")
                            .build()
            );
            Question question = Question.builder()
                    .member(member)
                    .content("질문 내용")
                    .receiveMbtiEntity(
                            MbtiEntity.builder()
                                    .attitude("I")
                                    .perception("N")
                                    .decision("F")
                                    .lifestyle("P")
                                    .build())
                    .build();
            given(memberService.findById(anyLong()))
                    .willReturn(member);
            given(questionService.findById(anyLong()))
                    .willReturn(question);

            //when

            //then
            assertThatThrownBy(() ->
                    answerFacade.create(answerRequest, 1L))
                    .isInstanceOf(QmaApiException.class)
                    .hasMessageContaining(ErrorCode.NOT_MATCHED_MBTI.getMessage());
        }
    }


}