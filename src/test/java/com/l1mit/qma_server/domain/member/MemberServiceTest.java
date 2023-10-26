package com.l1mit.qma_server.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.repository.MemberJpaRepository;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberJpaRepository memberJpaRepository;

    @AfterEach
    void clean() {
        memberJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("Id를 통해 Member를 찾는다.")
    void findById() {
        //given
        Long id = 1L;
        Member member = Member.builder()
                .oauth2Entity(
                        Oauth2Entity.builder()
                                .accountId("123123123")
                                .socialProvider(SocialProvider.valueOf("KAKAO"))
                                .build())
                .build();
        given(memberRepository.findById(id))
                .willReturn(Optional.of(member));

        //when
        Member byId = memberService.findById(id);

        //then
        assertThat(member).isEqualTo(byId);
    }

    @Test
    @DisplayName("Provider와 AccountId를 통해 Member를 찾는다")
    void findOauth2Account() {
        //given
        Member member = Member.builder()
                .oauth2Entity(
                        Oauth2Entity.builder()
                                .accountId("1")
                                .socialProvider(SocialProvider.valueOf("KAKAO"))
                                .build())
                .build();
        given(memberRepository.findByOauth2AccountId(SocialProvider.valueOf("KAKAO"), "1"))
                .willReturn(Optional.of(member));

        //when
        Member findMember = memberService.findOauth2Account(SocialProvider.valueOf("KAKAO"),
                        "1")
                .orElseThrow(() -> new QmaApiException(ErrorCode.NOT_FOUND));

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("Member 저장을 한다.")
    void save() {
        //given
        Oauth2Entity oauth2Entity = Oauth2Entity.builder()
                .accountId("1")
                .socialProvider(SocialProvider.valueOf("KAKAO"))
                .build();
        Member member = Member.builder()
                .oauth2Entity(oauth2Entity)
                .build();

        given(memberRepository.save(any()))
                .willReturn(member);

        //when
        Member saveMember = memberService.save(oauth2Entity);

        //then
        assertThat(saveMember).isEqualTo(member);
    }
}