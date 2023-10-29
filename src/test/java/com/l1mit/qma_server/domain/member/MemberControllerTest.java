package com.l1mit.qma_server.domain.member;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1mit.qma_server.domain.member.dto.SignInRequest;
import com.l1mit.qma_server.global.auth.AuthService;
import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.config.SecurityConfig;
import com.l1mit.qma_server.global.filter.JwtAuthenticationFilter;
import com.l1mit.qma_server.global.jwt.dto.JwtResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class MemberControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final SignInRequest signInRequest = SignInRequest.builder()
            .code("authCode")
            .redirectUri("http://redirect.uri")
            .build();
    private final JwtResponse jwtResponse = JwtResponse.builder()
            .accessToken("AccessToken")
            .refreshToken("RefreshToken")
            .build();
    private final IdTokenResponse idTokenResponse = IdTokenResponse.builder()
            .idToken("IdToken")
            .build();

    @Test
    @WithMockUser
    @DisplayName("Oauth2 로그인에 성공한다.")
    void signIn() throws Exception {
        //given
        String PROVIDER = "PROVIDER";
        given(authService.signIn(PROVIDER, signInRequest)).willReturn(idTokenResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/" + PROVIDER + "/sign-in")
                .content(objectMapper.writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }
}