package com.l1mit.qma_server.domain.chat.room.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.l1mit.qma_server.domain.chat.room.dto.param.ChatRoomParam;
import com.l1mit.qma_server.domain.chat.room.dto.request.ChatRoomRequest;
import com.l1mit.qma_server.domain.chat.room.dto.response.ChatRoomResponse;
import com.l1mit.qma_server.domain.chat.room.service.ChatRoomService;
import com.l1mit.qma_server.setting.docs.RestDocsControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = ChatRoomController.class)
class ChatRoomControllerTest extends RestDocsControllerTest {

    @MockBean
    ChatRoomService chatRoomService;

    @Nested
    class createChatRoom {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            ChatRoomRequest chatRoomRequest = ChatRoomRequest.builder()
                    .title("방 제목")
                    .build();

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/chat-room")
                    .content(toJson(chatRoomRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then

            resultActions
                    .andExpect(status().isNoContent())
                    .andDo(document("chatroom-create",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("채팅방")
                                            .summary("생성")
                                            .description("채팅방 생성 API")
                                            .requestFields(
                                                    fieldWithPath("title").type(JsonFieldType.STRING)
                                                            .description("방 제목")
                                            )
                                            .requestSchema(Schema.schema("ChatRoomRequest"))
                                            .build()
                                    )
                            )
                    );

        }
    }

    @Nested
    class searchChatRoom {

        @Test
        @DisplayName("성공한다.")
        void success() throws Exception {
            //given
            ChatRoomParam chatRoomParam = ChatRoomParam.builder()
                    .roomTitle("방 제목")
                    .build();
            PageRequest pageRequest = PageRequest.of(0, 10);

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

            given(chatRoomService.searchChatRoom(pageRequest, chatRoomParam))
                    .willReturn(new PageImpl<>(chatRooms, pageRequest, chatRooms.size()));

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/chat-room")
                    .queryParam("page", String.valueOf(pageRequest.getPageNumber()))
                    .queryParam("size", String.valueOf(pageRequest.getPageSize()))
                    .queryParam("roomTitle", chatRoomParam.roomTitle())
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(document("chatroom-search",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    resource(ResourceSnippetParameters.builder()
                                            .tag("채팅방")
                                            .summary("검색")
                                            .description("채팅방 검색 API")
                                            .queryParameters(
                                                    parameterWithName("page").optional().type(SimpleType.NUMBER).defaultValue(0)
                                                            .description("페이지"),
                                                    parameterWithName("size").optional().type(SimpleType.NUMBER).defaultValue(10)
                                                            .description("페이지 사이즈"),
                                                    parameterWithName("roomTitle").optional().type(SimpleType.NUMBER)
                                                            .description("방 제목")
                                            )
                                            .responseFields(
                                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                            .description("응답 데이터"),
                                                    fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                                                            .description("페이지 데이터"),
                                                    fieldWithPath("data.content[].roomId").type(JsonFieldType.STRING)
                                                            .description("방 ID"),
                                                    fieldWithPath("data.content[].roomTitle").type(JsonFieldType.STRING)
                                                            .description("방 제목"),
                                                    fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING)
                                                            .description("방 생성일"),
                                                    fieldWithPath("data.totalElement").type(JsonFieldType.NUMBER)
                                                            .description("페이지 전체 데이터 개수"),
                                                    fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                                            .description("페이지 전체 페이지 수"),
                                                    fieldWithPath("message").type(JsonFieldType.NULL)
                                                            .description("응답 데이터")
                                                    ,fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                                            .description("응답 데이터")
                                            )
                                            .build()
                                    )
                            )
                    );;
        }
    }
}