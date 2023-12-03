package com.l1mit.qma_server.domain.chat.participant.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.domain.chat.participant.domain.ChatParticipant;
import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.chat.room.repository.ChatRoomRepository;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.repository.MemberRepository;
import com.l1mit.qma_server.global.config.JpaConfig;
import com.l1mit.qma_server.setting.jpa.QueryDslConfig;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

@Import({QueryDslConfig.class, JpaConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ChatParticipantCustomRepositoryImplTest {

    @Autowired
    ChatParticipantRepository chatParticipantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Nested
    class existsByMemberIdAndRoomId {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            ChatRoom chatRoom = getChatRoom("방 제목");
            ReflectionTestUtils.setField(chatRoom, "roomId", "MockRoomId");
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

            Member member = getMember("123", SocialProvider.KAKAO);
            Member savedMember = memberRepository.save(member);

            ChatParticipant chatParticipant = ChatParticipant.builder()
                    .chatRoom(savedChatRoom)
                    .member(savedMember)
                    .build();

            chatParticipantRepository.save(chatParticipant);

            //when
            Boolean result = chatParticipantRepository.existsByMemberIdAndRoomId(savedChatRoom.getId(), "MockRoomId");

            //then
            assertThat(result).isTrue();
        }
    }

    @Nested
    class deleteByMemberIdAndRoomId {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            ChatRoom chatRoom = getChatRoom("방 제목");
            ReflectionTestUtils.setField(chatRoom, "roomId", "MockRoomId");
            chatRoomRepository.save(chatRoom);

            Member member = getMember("123", SocialProvider.KAKAO);
            Member savedMember = memberRepository.save(member);

            ChatParticipant chatParticipant = ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .member(member)
                    .build();

            chatParticipantRepository.save(chatParticipant);

            //when
            Long count = chatParticipantRepository.deleteByMemberIdAndRoomId(savedMember.getId(), "MockRoomId");

            //then
            assertThat(count).isEqualTo(1L);
        }

        @Test
        @DisplayName("존재하지 않는 값을 삭제한다면 0L을 반환한다.")
        void fail() {
            //given
            ChatRoom chatRoom = getChatRoom("방 제목");
            ReflectionTestUtils.setField(chatRoom, "roomId", "MockRoomId");
            chatRoomRepository.save(chatRoom);

            Member member = getMember("123", SocialProvider.KAKAO);
            Member savedMember = memberRepository.save(member);

            ChatParticipant chatParticipant = ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .member(member)
                    .build();

            chatParticipantRepository.save(chatParticipant);

            //when
            Long count = chatParticipantRepository.deleteByMemberIdAndRoomId(savedMember.getId() + 1, "RoomId");

            //then
            assertThat(count).isEqualTo(0L);
        }
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

    private ChatRoom getChatRoom(String roomTitle) {
        return ChatRoom.builder()
                .roomTitle(roomTitle)
                .build();
    }
}