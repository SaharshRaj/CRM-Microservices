package com.crm.api_gateway.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


/**
 * Aspect for logging execution of service and controller Spring components.
 */

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */

    @Pointcut("execution(* com.crm.service..*(..))")
    public void serviceLayer() {

    }

    /**
     * Pointcut that matches all controllers.
     */

    @Pointcut("execution(* com.crm.controller..*(..))")
    public void controllerLayer() {

    }

    /**
     * Advice that logs when a method in the service layer is entered.
     *
     * @param joinPoint join point for advice
     */

    @Before("serviceLayer()")
    public void beforeServiceMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Entering Service Method: {} with arguments: {}", methodName, args);
    }

    /**
     * Advice that logs when a method in the service layer successfully returns.
     *
     * @param joinPoint join point for advice
     * @param result the result of the method execution
     */

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void afterServiceMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Service Method: {} executed successfully with result: {}", methodName, result);
    }

    /**
     * Advice that logs when a method in the service layer throws an exception.
     *
     * @param joinPoint join point for advice
     * @param exception the thrown exception
     */

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void afterServiceMethodException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Service Method: {} threw an exception: {}", methodName, exception.getMessage());
    }

    /**
     * Advice that logs when a method in the controller layer is entered.
     *
     * @param joinPoint join point for advice
     */

    @Before("controllerLayer()")
    public void beforeControllerMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Entering Controller Method: {} with arguments: {}", methodName, args);
    }

    /**
     * Advice that logs when a method in the controller layer successfully returns.
     *
     * @param joinPoint join point for advice
     * @param result the result of the method execution
     */

    @AfterReturning(pointcut = "controllerLayer()", returning = "result")
    public void afterControllerMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Controller Method: {} executed successfully with result: {}", methodName, result);
    }

    /**
     * Advice that logs when a method in the controller layer throws an exception.
     *
     * @param joinPoint join point for advice
     * @param exception the thrown exception
     */
    @AfterThrowing(pointcut = "controllerLayer()", throwing = "exception")
    public void afterControllerMethodException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Controller Method: {} threw an exception: {}", methodName, exception.getMessage());
    }
}