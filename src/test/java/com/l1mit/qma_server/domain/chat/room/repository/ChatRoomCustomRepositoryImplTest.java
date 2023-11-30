package com.l1mit.qma_server.domain.chat.room.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.l1mit.qma_server.domain.chat.room.domain.ChatRoom;
import com.l1mit.qma_server.domain.chat.room.dto.param.ChatRoomParam;
import com.l1mit.qma_server.domain.chat.room.dto.response.ChatRoomResponse;
import com.l1mit.qma_server.global.config.JpaConfig;
import com.l1mit.qma_server.setting.jpa.QueryDslConfig;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@Import({QueryDslConfig.class, JpaConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ChatRoomCustomRepositoryImplTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Nested
    class findByRoomId {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            String mockRoomId = "mockRoomId";

            ChatRoom chatRoom = ChatRoom.builder()
                    .roomTitle("방 제목")
                    .build();
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
            ReflectionTestUtils.setField(savedChatRoom, "roomId", mockRoomId);

            //when
            Optional<ChatRoom> result = chatRoomRepository.findByRoomId(mockRoomId);

            //then
            assertThat(result.get()).isNotNull();
            assertThat(result.get().getRoomId()).isEqualTo(mockRoomId);
        }
    }

    @Nested
    class searchByCondition {

        @Test
        @DisplayName("성공한다.")
        void success() {
            //given
            PageRequest pageRequest = PageRequest.of(0, 10);
            ChatRoomParam chatRoomParam = ChatRoomParam.builder()
                    .roomTitle("mockRoomTitle")
                    .build();

            ChatRoom chatRoom1 = ChatRoom.builder()
                    .roomTitle("mockRoomTitle")
                    .build();
            chatRoomRepository.save(chatRoom1);
            ChatRoom chatRoom2 = ChatRoom.builder()
                    .roomTitle("otherMockRoomTitle")
                    .build();
            chatRoomRepository.save(chatRoom2);

            //when
            Page<ChatRoomResponse> result = chatRoomRepository.searchByCondition(pageRequest, chatRoomParam);

            //then
            assertThat(result.getContent()).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
        }
    }
}