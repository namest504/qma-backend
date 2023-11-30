package com.l1mit.qma_server.domain.chat.participant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.l1mit.qma_server.domain.chat.participant.domain.ChatParticipant;
import com.l1mit.qma_server.domain.chat.participant.repository.ChatParticipantRepository;
import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatParticipantServiceTest {

    @InjectMocks
    ChatParticipantService chatParticipantService;

    @Mock
    ChatParticipantRepository chatParticipantRepository;

    @Nested
    class save {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            Member member = getMember("123", SocialProvider.KAKAO);
            ChatRoom chatRoom = getChatRoom("roomTitle");

            //when
            chatParticipantService.save(chatRoom, member);

            //then
            verify(chatParticipantRepository, times(1)).save(any(ChatParticipant.class));
        }
    }

    @Nested
    class existsByMemberIdAndRoomId {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            given(chatParticipantRepository.existsByMemberIdAndRoomId(anyLong(), anyString()))
                    .willReturn(Boolean.TRUE);

            //when
            Boolean result = chatParticipantService.existsByMemberIdAndRoomId(anyLong(), anyString());

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
            given(chatParticipantRepository.deleteByMemberIdAndRoomId(anyLong(), anyString()))
                    .willReturn(1L);

            //when
            chatParticipantService.deleteByMemberIdAndRoomId(anyLong(), anyString());

            //then
            verify(chatParticipantRepository, times(1)).deleteByMemberIdAndRoomId(anyLong(), anyString());
        }

        @Test
        @DisplayName("삭제 대상이 없다면 예외 처리를 한다.")
        void success_throwException() {
            //given
            given(chatParticipantRepository.deleteByMemberIdAndRoomId(anyLong(), anyString()))
                    .willReturn(0L);

            //when

            //then
            assertThatThrownBy(() ->
                    chatParticipantService.deleteByMemberIdAndRoomId(anyLong(), anyString()))
                    .isInstanceOf(QmaApiException.class)
                    .hasMessageContaining(ErrorCode.NOT_FOUND.getMessage());
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