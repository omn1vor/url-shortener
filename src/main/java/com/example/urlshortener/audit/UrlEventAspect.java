package com.example.urlshortener.audit;

import com.example.urlshortener.audit.event.UrlEventEntry;
import com.example.urlshortener.audit.event.UrlEventType;
import com.example.urlshortener.model.ShortenedUrl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class UrlEventAspect {

    @Autowired
    ApplicationContext context;

    @Pointcut("execution(* com.example.urlshortener.service.ShortenedUrlService*.addUrl(..))")
    public void urlCreated() {
    }

    @Pointcut("execution(* com.example.urlshortener.service.ShortenedUrlService*.deactivate(..))")
    public void urlDisabled() {
    }

    @Pointcut("execution(* com.example.urlshortener.service.ShortenedUrlService*.activate(..))")
    public void urlModified() {
    }

    @Around("urlCreated()")
    public Object logCreation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof ShortenedUrl url) {
            context.publishEvent(
                    new UrlEventEntry(url, UrlEventType.CREATED,
                            String.format("Short URL %s created by user %s", url.getShortUrl(), url.getEmail())
                    )
            );
        }

        return result;
    }

    @Around("urlDisabled()")
    public Object logDeactivating(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof ShortenedUrl url) {
            context.publishEvent(
                    new UrlEventEntry(url, UrlEventType.DISABLED,
                            String.format("Short URL %s disabled by user %s", url.getShortUrl(), url.getEmail())
                    )
            );
        }

        return result;
    }

    @Around("urlModified()")
    public Object logModification(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof ShortenedUrl url) {
            context.publishEvent(
                    new UrlEventEntry(url, UrlEventType.MODIFIED,
                            String.format("Short URL %s modified by user %s", url.getShortUrl(), url.getEmail())
                    )
            );
        }

        return result;
    }
}
