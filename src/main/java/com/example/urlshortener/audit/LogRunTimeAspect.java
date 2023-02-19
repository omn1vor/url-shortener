package com.example.urlshortener.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Configuration
@Aspect
@Slf4j
public class LogRunTimeAspect {

    @Pointcut("@annotation(com.example.urlshortener.audit.LogRunTime)")
    public void methodsToLogRunTime() {}

    @Around("methodsToLogRunTime()")
    public Object logRunTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info(String.format("Method %s started", joinPoint.toShortString()));

        Object result = joinPoint.proceed();

        stopWatch.stop();
        log.info(String.format("Method %s finished. Run time in ms: %d",
                joinPoint.toShortString(),
                stopWatch.getTotalTimeMillis()));

        return result;
    }
}
