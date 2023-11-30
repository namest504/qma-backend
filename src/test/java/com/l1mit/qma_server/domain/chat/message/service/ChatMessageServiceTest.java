package com.l1mit.qma_server.domain.chat.message.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.l1mit.qma_server.domain.chat.message.domain.ChatMessage;
import com.l1mit.qma_server.domain.chat.message.dto.request.ChatMessageRequest;
import com.l1mit.qma_server.domain.chat.message.dto.response.ChatMessageResponse;
import com.l1mit.qma_server.domain.chat.message.mapper.ChatMessageMapper;
import com.l1mit.qma_server.domain.chat.message.repository.ChatMessageRepository;
import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.chat.room.service.ChatRoomService;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.domain.member.service.MemberService;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @InjectMocks
    ChatMessageService chatMessageService;

    @Mock
    MemberService memberService;

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Mock
    ChatRoomService chatRoomService;

    @Mock
    ChatMessageMapper chatMessageMapper;

    @Nested
    class saveChatMessage {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            Member member = getMember("123", SocialProvider.KAKAO);
            ReflectionTestUtils.setField(member,"id", 1L);

            ChatRoom chatRoom = getChatRoom("roomTitle");
            ReflectionTestUtils.setField(chatRoom,"roomId", "mockRoomId");

            ChatMessageRequest chatMessageRequest = ChatMessageRequest.builder()
                    .content("메세지 내용")
                    .roomId("mockRoomId")
                    .build();
            ChatMessageResponse chatMessageResponse = ChatMessageResponse.builder()
                    .messageContent("메세지 내용")
                    .messageId(1L)
                    .nickname("닉네임")
                    .userId(1L)
                    .createdAt(LocalDateTime.now())
                    .build();

            given(memberService.findById(anyLong()))
                    .willReturn(member);
            given(chatRoomService.getByRoomId(anyString()))
                    .willReturn(chatRoom);
            given(chatMessageMapper.entityToChatMessageResponse(any()))
                    .willReturn(chatMessageResponse);
            //when
            ChatMessageResponse result = chatMessageService.saveChatMessage(member.getId(), chatMessageRequest);

            //then
            assertThat(result).isNotNull();
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