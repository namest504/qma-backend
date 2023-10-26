package com.l1mit.qma_server.global.common;

import com.l1mit.qma_server.global.auth.oauth.dto.PrincipalUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
public class MemberIdArgumentsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberId.class)
                && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        return getPrincipal().getId();
//        return (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().
    }

    private static PrincipalUser getPrincipal() {
        return (PrincipalUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    }
}