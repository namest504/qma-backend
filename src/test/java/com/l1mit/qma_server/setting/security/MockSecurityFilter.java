package com.l1mit.qma_server.setting.security;

import com.l1mit.qma_server.domain.member.domain.Member;
import com.l1mit.qma_server.domain.member.domain.Oauth2Entity;
import com.l1mit.qma_server.domain.member.domain.enums.SocialProvider;
import com.l1mit.qma_server.global.auth.oauth.dto.PrincipalUser;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class MockSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();

        PrincipalUser principalUser = new PrincipalUser(createMember());
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(),
                        principalUser.getAuthorities()));

        chain.doFilter(request, response);
    }

    private static Member createMember() {
        Oauth2Entity oauth2Entity = Oauth2Entity.builder()
                .accountId("su41kvy9q")
                .socialProvider(SocialProvider.GOOGLE)
                .build();
        Member member = Member.builder()
                .oauth2Entity(oauth2Entity)
                .build();

        Class<Member> memberClass = Member.class;
        try {
            Field id = memberClass.getDeclaredField("id");
            id.setAccessible(true);
            id.set(member, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return member;
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }

    public void getFilters(MockHttpServletRequest mockHttpServletRequest) {

    }
}
