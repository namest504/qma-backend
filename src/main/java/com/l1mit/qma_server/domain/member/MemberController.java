package com.l1mit.qma_server.domain.member;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.dto.MemberInfoResponse;
import com.l1mit.qma_server.domain.member.dto.SignInRequest;
import com.l1mit.qma_server.global.auth.AuthService;
import com.l1mit.qma_server.global.auth.oauth.dto.PrincipalUser;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import com.l1mit.qma_server.global.jwt.dto.JwtResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping("/{provider}/sign-in")
    public ResponseEntity<ApiResponse<JwtResponse>> signIn(
            @PathVariable("provider") String provider,
            @RequestBody @Valid SignInRequest signInRequest) {
        JwtResponse jwtResponse = authService.signIn(provider, signInRequest);
        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithData(jwtResponse));
    }

    @GetMapping("/my-info")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> myInfo(
            @AuthenticationPrincipal PrincipalUser principalUser) {
        log.info("id = {}", principalUser.getId());
        Member member = memberService.findById(principalUser.getId());
        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithData(member.toMemberResponse()));
    }

}
