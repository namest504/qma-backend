package com.l1mit.qma_server.global.config;

import com.l1mit.qma_server.global.exception.StompErrorHandler;
import com.l1mit.qma_server.global.common.interceptor.StompInterceptor;
import com.l1mit.qma_server.global.jwt.JwtValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtValidator jwtValidator;
    private final StompErrorHandler stompErrorHandler;

    public StompConfig(final JwtValidator jwtValidator, StompErrorHandler stompErrorHandler) {
        this.jwtValidator = jwtValidator;
        this.stompErrorHandler = stompErrorHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(stompErrorHandler)
                .addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/pub");
        registry.setApplicationDestinationPrefixes("/stomp");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new StompInterceptor(jwtValidator));
    }

}
