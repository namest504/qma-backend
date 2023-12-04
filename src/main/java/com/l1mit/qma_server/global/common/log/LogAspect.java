package com.l1mit.qma_server.global.common.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.l1mit.qma_server..*Controller.*(..))")
    public void controllerPoint() {
    }

    @Before("controllerPoint()")
    public void requestLogging(JoinPoint joinPoint) {
        log.info("REQUEST [CLASS : {}] [METHOD : {}] [ARGS : {}]",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controllerPoint()", returning = "returnValue")
    public void responseLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("RESPONSE [RESULT : {}]", returnValue);
    }
}
