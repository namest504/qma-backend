package com.l1mit.qma_server.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.setting.jpa.QueryDslConfig;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Member를 Id를 통해 찾는데 성공한다.")
    void findById_existMember_success() {
        // given
        Member member = Member.builder()
                .oauth2Entity(
                        Oauth2Entity.builder()
                                .accountId("123123123")
                                .socialProvider(SocialProvider.valueOf("KAKAO"))
                                .build())
                .build();
        Member save = memberRepository.save(member);

        // when
        Member result = memberRepository.findById(save.getId()).get();

        // then
        assertThat(result).isNotNull();  // 결과가 null이 아닌지 검증
        assertThat(member).isEqualTo(result);  // 결과가 예상된 객체인지 검증
    }

    @Test
    @DisplayName("존재하지 않는 유저를 검색하면 Optional Empty를 반환한다.")
    void findById_noExistMember_returnEmpty() {
        Optional<Member> member = memberRepository.findById(1L);

        assertThat(member).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("Oauth2로 가입된 유저를 AccountId로 찾는데 성공한다.")
    void findByOauth2AccountId() {
        // given
        Member member = Member.builder()
                .oauth2Entity(
                        Oauth2Entity.builder()
                                .accountId("123123123")
                                .socialProvider(SocialProvider.valueOf("KAKAO"))
                                .build())
                .build();
        Member save = memberRepository.save(member);

        //when
        Optional<Member> result = memberRepository.findByOauth2AccountId(
                SocialProvider.valueOf("KAKAO"), "123123123");

        //then
        assertThat(result).isNotNull();
        assertThat(save).isEqualTo(result.get());
    }
}