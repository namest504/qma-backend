package com.l1mit.qma_server.domain.answer.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.domain.answer.Answer;
import com.l1mit.qma_server.domain.answer.dto.AnswerResponse;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.domain.question.Question;
import com.l1mit.qma_server.domain.question.repository.QuestionRepository;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.setting.jpa.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswerCustomRepositoryImplTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Nested
    @DisplayName("findPagedAnswerByQuestionId 메소드는")
    class findPagedAnswerByQuestionId {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            Integer pageNumber = 0;
            Integer pageSize = 10;
            Long questionId = 1L;

            Member member = Member.builder()
                    .oauth2Entity(
                            Oauth2Entity.builder()
                                    .accountId("123")
                                    .socialProvider(SocialProvider.KAKAO)
                                    .build()
                    )
                    .build();
            Member savedMember = memberRepository.save(member);
            Question question = Question.builder()
                    .member(savedMember)
                    .content("질문 내용")
                    .receiveMbtiEntity(
                            MbtiEntity.builder()
                                    .attitude("E")
                                    .perception("N")
                                    .decision("T")
                                    .lifestyle("P")
                                    .build()
                    )
                    .build();
            Question savedQuestion = questionRepository.save(question);
            Answer answer1 = Answer.builder()
                    .member(savedMember)
                    .question(savedQuestion)
                    .content("대답 내용")
                    .build();
            answerRepository.save(answer1);
            Answer answer2 = Answer.builder()
                    .member(savedMember)
                    .question(savedQuestion)
                    .content("대답 내용")
                    .build();
            answerRepository.save(answer2);

            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

            //when
            Page<AnswerResponse> pagedAnswerByQuestionId = answerRepository.findPagedAnswerByQuestionId(
                    pageRequest, questionId);
            //then
            assertThat(pagedAnswerByQuestionId.getContent()).hasSize(2);
            assertThat(pagedAnswerByQuestionId.getTotalPages()).isEqualTo(1);
            assertThat(pagedAnswerByQuestionId.getNumberOfElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("없는 게시글이라면 없는 게시글 반환하고 성공한다.")
        void success_NotFound() throws Exception {
            //given
            Integer pageNumber = 0;
            Integer pageSize = 10;
            Long questionId = 1L;
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

            //when
            Page<AnswerResponse> pagedAnswerByQuestionId = answerRepository.findPagedAnswerByQuestionId(
                    pageRequest, questionId);

            //then
            assertThat(pagedAnswerByQuestionId.getContent()).hasSize(0);
        }
    }
}