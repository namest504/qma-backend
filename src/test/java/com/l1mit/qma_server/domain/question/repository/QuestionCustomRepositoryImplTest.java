package com.l1mit.qma_server.domain.question.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.domain.answer.repository.AnswerRepository;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.domain.question.domain.Question;
import com.l1mit.qma_server.domain.question.dto.param.QuestionSearchParam;
import com.l1mit.qma_server.domain.question.dto.response.QuestionDetailResponse;
import com.l1mit.qma_server.domain.question.dto.response.QuestionResponse;
import com.l1mit.qma_server.global.common.domain.MBTI;
import com.l1mit.qma_server.global.common.domain.MbtiEntity;
import com.l1mit.qma_server.global.config.JpaConfig;
import com.l1mit.qma_server.setting.jpa.QueryDslConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Import({QueryDslConfig.class, JpaConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class QuestionCustomRepositoryImplTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    MemberRepository memberRepository;

    LocalDate currentDate;

    PageRequest pageRequest = getPageRequest(0, 10);

    @BeforeEach
    void setUp() {
        currentDate = LocalDate.now();
    }

    @Nested
    @DisplayName("searchWithCondition 메소드는")
    class searchWithCondition {

        @BeforeEach
        void setUp() {
            Member member1 = getMember("1111", SocialProvider.KAKAO);
            Member member2 = getMember("2222", SocialProvider.GOOGLE);
            member1.updateMbtiEntity(getMbtiEntity("ISTJ"));
            member2.updateMbtiEntity(getMbtiEntity("ENTP"));
            memberRepository.save(member1);
            memberRepository.save(member2);
            Question question1 = getQuestion("질문 내용1", "INTP", member1);
            Question question2 = getQuestion("질문 내용2", "ENTJ", member2);
            Question question3 = getQuestion("질문 내용3", "ENTP", member1);
            Question question4 = getQuestion("질문 내용4", "INTJ", member2);
            Question question5 = getQuestion("질문 내용5", "ESTJ", member1);
            Question question6 = getQuestion("질문 내용6", "ENFJ", member2);
            questionRepository.save(question1);
            questionRepository.save(question2);
            questionRepository.save(question3);
            questionRepository.save(question4);
            questionRepository.save(question5);
            questionRepository.save(question6);
        }

        @Test
        @DisplayName("검색 조건이 없어도 성공한다.")
        void success_NoCondition() {
            //given
            QuestionSearchParam param = QuestionSearchParam.builder()
                    .build();

            //when
            Page<QuestionResponse> questionResponses = questionRepository.searchWithCondition(pageRequest, param);
            //then
            assertThat(questionResponses.getContent().size()).isEqualTo(6);
        }

        @Test
        @DisplayName("질문자 이름 검색을 통해 성공한다.")
        void success_WriterCondition() {
            //given
            QuestionSearchParam param = QuestionSearchParam.builder()
                    .writer("1111")
                    .build();

            //when
            Page<QuestionResponse> questionResponses = questionRepository.searchWithCondition(pageRequest, param);

            //then
            assertThat(questionResponses.getContent().size()).isEqualTo(3);
        }

        @Test
        @DisplayName("대상 MBTI 검색을 통해 성공한다.")
        void success_ReceiveMbtiCondition() {
            //given
            QuestionSearchParam param = QuestionSearchParam.builder()
                    .receiveMbti("ENTJ")
                    .build();

            //when
            Page<QuestionResponse> questionResponses = questionRepository.searchWithCondition(pageRequest, param);

            //then
            assertThat(questionResponses.getContent().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("작성자 MBTI 검색을 통해 성공한다.")
        void success_SendMbtiCondition() {
            //given
            QuestionSearchParam param = QuestionSearchParam.builder()
                    .sendMbti("ENTP")
                    .build();

            //when
            Page<QuestionResponse> questionResponses = questionRepository.searchWithCondition(pageRequest, param);

            //then
            assertThat(questionResponses.getContent().size()).isEqualTo(3);
        }

        @Test
        @DisplayName("작성일을 통해 성공한다.")
        void success_TimeCondition() {
            //given
            QuestionSearchParam param = QuestionSearchParam.builder()
                    .startTime(currentDate.minusDays(1))
                    .endTime(currentDate.plusDays(1))
                    .build();

            //when
            Page<QuestionResponse> questionResponses = questionRepository.searchWithCondition(pageRequest, param);

            //then
            assertThat(questionResponses.getContent().size()).isEqualTo(6);
        }
    }

    @Nested
    @DisplayName("findByIdWithDetail 메소드는")
    class findByIdWithDetail {

        @Test
        @DisplayName("값 반환에 성공한다.")
        void success() {
            //given
            Member member = getMember("1111", SocialProvider.KAKAO);
            member.updateMbtiEntity(getMbtiEntity("ISTJ"));
            memberRepository.save(member);
            Question question1 = getQuestion("질문 내용1", "INTP", member);
            Question savedQuestion = questionRepository.save(question1);

            //when
            QuestionDetailResponse questionDetailResponse = questionRepository.findByIdWithDetail(savedQuestion.getId()).get();

            //then
            assertThat(questionDetailResponse).isNotNull();
            assertThat(questionDetailResponse.id()).isEqualTo(savedQuestion.getId());
        }
    }


    @NotNull
    private PageRequest getPageRequest(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize);
    }

    private Question getQuestion(String content, String mbti, Member member) {
        return Question.builder()
                .member(member)
                .content(content)
                .receiveMbtiEntity(
                        getMbtiEntity(mbti))
                .build();
    }

    private Member getMember(String accountId, SocialProvider socialProvider) {
        return Member.builder()
                .oauth2Entity(getOauth2Entity(accountId, socialProvider))
                .build();
    }

    private Oauth2Entity getOauth2Entity(String accountId, SocialProvider socialProvider) {
        return Oauth2Entity.builder()
                .accountId(accountId)
                .socialProvider(socialProvider)
                .build();
    }

    private QuestionResponse getQuestionResponse(Long id, String writer, LocalDateTime localDateTime, String mbti) {
        return QuestionResponse.builder()
                .id(id)
                .writer(writer)
                .createdAt(localDateTime)
                .mbti(MBTI.valueOf(mbti))
                .build();
    }

    private MbtiEntity getMbtiEntity(String mbti) {
        return MbtiEntity.builder()
                .mbti(MBTI.valueOf(mbti))
                .build();
    }
}