package com.l1mit.qma_server.domain.chat.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.l1mit.qma_server.domain.chat.participant.service.ChatParticipantService;
import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.chat.room.dto.param.ChatRoomParam;
import com.l1mit.qma_server.domain.chat.room.dto.request.ChatRoomRequest;
import com.l1mit.qma_server.domain.chat.room.dto.response.ChatRoomResponse;
import com.l1mit.qma_server.domain.chat.room.repository.ChatRoomRepository;
import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.exception.ErrorCode;
import com.l1mit.qma_server.global.exception.QmaApiException;
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
class ChatRoomServiceTest {

    @InjectMocks
    ChatRoomService chatRoomService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    ChatParticipantService chatParticipantService;

    @Nested
    class createChatRoom {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            ChatRoomRequest chatRoomRequest = ChatRoomRequest.builder()
                    .title("방 제목")
                    .build();

            //when
            chatRoomService.createChatRoom(chatRoomRequest);

            //then
            verify(chatRoomRepository, times(1)).save(any());
        }
    }

    @Nested
    class getByRoomId {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            ChatRoom chatRoom = ChatRoom.builder()
                    .roomTitle("방 제목")
                    .build();
            ReflectionTestUtils.setField(chatRoom, "roomId", "mockRoomId");
            given(chatRoomRepository.findByRoomId(anyString()))
                    .willReturn(Optional.ofNullable(chatRoom));

            //when
            ChatRoom result = chatRoomService.getByRoomId("mockRoomId");

            //then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("roomId가 일치하는 ChatRoom이 없으면 예외처리한다.")
        void fail_NotFound() {
            //given

            //when

            //then
            assertThatThrownBy(() ->
                    chatRoomService.getByRoomId("mockRoomId"))
                    .isInstanceOf(QmaApiException.class)
                    .hasMessageContaining(ErrorCode.NOT_FOUND.getMessage());
        }
    }

    @Nested
    class searchChatRoom {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            PageRequest pageRequest = PageRequest.of(0, 10);

            ChatRoomParam param = ChatRoomParam.builder()
                    .roomTitle("roomTitle1")
                    .build();

            ChatRoomResponse chatRoomResponse1 = ChatRoomResponse.builder()
                    .roomId("roomId1")
                    .roomTitle("roomTitle1")
                    .createdAt(LocalDateTime.now())
                    .build();
            ChatRoomResponse chatRoomResponse2 = ChatRoomResponse.builder()
                    .roomId("roomId2")
                    .roomTitle("roomTitle2")
                    .createdAt(LocalDateTime.now())
                    .build();
            List<ChatRoomResponse> chatRooms = List.of(chatRoomResponse1, chatRoomResponse2);

            given(chatRoomRepository.searchByCondition(pageRequest, param))
                    .willReturn(new PageImpl<>(chatRooms, pageRequest, chatRooms.size()));
            //when
            Page<ChatRoomResponse> result = chatRoomService.searchChatRoom(pageRequest, param);

            //then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getTotalElements()).isEqualTo(2);
        }
    }

    @Nested
    class storeSubscribeInfo {

        @Test
        @DisplayName("세션이 저장되어있지 않다면 저장하고 성공한다.")
        void success_save() {
            //given
            ChatRoom chatRoom = ChatRoom.builder()
                    .roomTitle("방 제목")
                    .build();

            Member member = getMember(getOauth2Entity("123", SocialProvider.KAKAO));
            ReflectionTestUtils.setField(member, "id", 1L);

            given(chatRoomRepository.findByRoomId(anyString()))
                    .willReturn(Optional.of(chatRoom));
            given(chatParticipantService.existsByMemberIdAndRoomId(anyLong(), anyString()))
                    .willReturn(Boolean.FALSE);

            //when
            chatRoomService.storeSubscribeInfo(member, "mockRoomId");

            //then
            verify(chatParticipantService, times(1)).save(any(), any());
        }

        @Test
        @DisplayName("세션이 저장되어있다면 저장 하지않고 성공한다.")
        void success_NoSave() {
            //given
            ChatRoom chatRoom = ChatRoom.builder()
                    .roomTitle("방 제목")
                    .build();

            Member member = getMember(getOauth2Entity("123", SocialProvider.KAKAO));
            ReflectionTestUtils.setField(member, "id", 1L);

            given(chatRoomRepository.findByRoomId(anyString()))
                    .willReturn(Optional.of(chatRoom));
            given(chatParticipantService.existsByMemberIdAndRoomId(anyLong(), anyString()))
                    .willReturn(Boolean.TRUE);

            //when
            chatRoomService.storeSubscribeInfo(member, "mockRoomId");

            //then
            verify(chatParticipantService, times(0)).save(any(), any());
        }
    }

    private Oauth2Entity getOauth2Entity(String accountId, SocialProvider socialProvider) {
        Oauth2Entity oauth2Entity = Oauth2Entity.builder()
                .accountId(accountId)
                .socialProvider(socialProvider)
                .build();
        return oauth2Entity;
    }

    private Member getMember(Oauth2Entity oauth2Entity) {
        Member member = Member.builder()
                .oauth2Entity(oauth2Entity)
                .build();
        return member;
    }
}