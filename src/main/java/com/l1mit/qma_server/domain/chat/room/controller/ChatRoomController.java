package com.l1mit.qma_server.domain.chat.room.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.l1mit.qma_server.domain.chat.room.service.ChatRoomService;
import com.l1mit.qma_server.domain.chat.room.dto.param.ChatRoomParam;
import com.l1mit.qma_server.domain.chat.room.dto.request.ChatRoomRequest;
import com.l1mit.qma_server.domain.chat.room.dto.response.ChatRoomResponse;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import com.l1mit.qma_server.global.common.response.PageResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(final ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createChatRoom(@RequestBody @Valid ChatRoomRequest chatRoomRequest) {

        chatRoomService.createChatRoom(chatRoomRequest);

        return ResponseEntity.status(NO_CONTENT)
                .body(ApiResponse.createSuccess());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<ChatRoomResponse>>>> searchChatRoom(@PageableDefault Pageable pageable, ChatRoomParam chatRoomParam) {
        Page<ChatRoomResponse> responses = chatRoomService.searchChatRoom(pageable, chatRoomParam);
        return ResponseEntity.status(OK)
                .body(ApiResponse.createSuccessWithData(PageResponse.create(
                        responses.getContent(),
                        responses.getNumberOfElements(),
                        responses.getTotalPages()
                )));
    }
}
