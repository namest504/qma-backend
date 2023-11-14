package com.l1mit.qma_server.domain.answer.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.domain.answer.domain.Answer;
import com.l1mit.qma_server.domain.answer.dto.response.AnswerResponse;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.repository.QuestionRepository;
import com.l1mit.qma_server.global.common.domain.MBTI;
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

        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            Member member = getMember(getOauth2Entity("123", SocialProvider.KAKAO));
            Member savedMember = memberRepository.save(member);

            Question question = Question.builder()
                    .member(savedMember)
                    .content("질문 내용")
                    .receiveMbtiEntity(
                            getMbtiEntity("ENTP")
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
                    .content("대답 내용1")
                    .build();
            answerRepository.save(answer2);

            //when
            Page<AnswerResponse> result = answerRepository.findPagedAnswerByQuestionId(pageRequest, savedQuestion.getId());

            //then
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getNumberOfElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("없는 게시글이라면 없는 게시글 반환하고 성공한다.")
        void success_NotFound() {
            //given
            Member member = getMember(getOauth2Entity("123", SocialProvider.KAKAO));
            Member savedMember = memberRepository.save(member);

            Question question = Question.builder()
                    .member(savedMember)
                    .content("질문 내용")
                    .receiveMbtiEntity(
                            getMbtiEntity("ENTP")
                    )
                    .build();
            Question savedQuestion = questionRepository.save(question);

            //when
            Page<AnswerResponse> result = answerRepository.findPagedAnswerByQuestionId(pageRequest, savedQuestion.getId());

            //then
            assertThat(result.getContent()).hasSize(0);
            assertThat(result.getTotalElements()).isEqualTo(0);
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

    private MbtiEntity getMbtiEntity(String mbti) {
        return MbtiEntity.builder()
                .mbti(MBTI.valueOf(mbti))
                .build();
    }
}