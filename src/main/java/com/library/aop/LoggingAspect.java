package com.library.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.library.service.BookService.*(..)) || " +
            "execution(* com.library.service.PatronService.*(..)) || " +
            "execution(* com.library.service.BorrowingService.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info("Entering {}.{}() with arguments: {}", className, methodName, joinPoint.getArgs());
        
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        logger.info("Exiting {}.{}() with result: {} (execution time: {} ms)", 
            className, methodName, result, elapsedTime);
        
        return result;
    }

    @Around("@annotation(com.library.aop.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        logger.info("Method {} executed in {} ms", 
            joinPoint.getSignature(), elapsedTime);
        
        return result;
    }
}