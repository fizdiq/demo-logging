package com.example.demologging.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * The Aspect config type.
 */
@Component
@Aspect
@Slf4j
public class AspectConfig {
    /**
     * The Class.
     */
    static final String CLASS = " in ";

    /**
     * Interceptor for services object.
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("execution(* com.example.demologging.services..*(..))")
    public Object interceptorForServices(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String className = getMethodSignatute(joinPoint).getDeclaringType().getSimpleName();
        String methodName = getMethodSignatute(joinPoint).getName();
        try {
            stopWatch.start();
            log.info("Start method " + methodName + CLASS + className);
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("Method " + methodName + CLASS + className + " done in " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }

    /**
     * Interceptor for controllers object.
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("execution(* com.example.demologging.controller..*(..))")
    public Object interceptorForControllers(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String className = getMethodSignatute(joinPoint).getDeclaringType().getSimpleName();
        String methodName = getMethodSignatute(joinPoint).getName();
        try {
            stopWatch.start();
            log.info("START Request " + methodName + CLASS + className);
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("END Request " + methodName + CLASS + className + " " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }

    /**
     * Gets method signatute.
     *
     * @param joinPoint the join point
     * @return the method signatute
     */
    private MethodSignature getMethodSignatute(ProceedingJoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }
}
