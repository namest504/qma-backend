package com.l1mit.qma_server.global.config;

import com.l1mit.qma_server.global.common.MemberIdArgumentsResolver;
import com.l1mit.qma_server.global.common.interceptor.LogInterceptor;
import com.l1mit.qma_server.global.converter.StringToNullConverter;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberIdArgumentsResolver memberIdArgumentsResolver;
    private final StringToNullConverter stringToNullConverter;
    private final LogInterceptor logInterceptor;

    public WebConfig(MemberIdArgumentsResolver memberIdArgumentsResolver, StringToNullConverter stringToNullConverter, LogInterceptor logInterceptor) {
        this.memberIdArgumentsResolver = memberIdArgumentsResolver;
        this.stringToNullConverter = stringToNullConverter;
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdArgumentsResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.OPTIONS.name());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToNullConverter);
    }
}
