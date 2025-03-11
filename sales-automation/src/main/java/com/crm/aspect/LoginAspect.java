package com.crm.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoginAspect {

        @Pointcut("execution(* com.crm.service..*(..))")
        public void serviceMethods() {}

        @Pointcut("execution(* com.crm.controller..*(..))")
        public void controllerMethods() {}

        @Before("serviceMethods()")
        public void logBeforeServiceMethodExecution(JoinPoint joinPoint) {
            String methodName = joinPoint.getSignature().getName();
            Object[] methodArgs = joinPoint.getArgs();
            log.info("Service Method {} is called with arguments: {}", methodName, methodArgs);
        }

        @AfterReturning(pointcut = "serviceMethods()")
        public void logAfterServiceMethodExecution(JoinPoint joinPoint) {
            String methodName = joinPoint.getSignature().getName();
            log.info("Service Method {} has returned.", methodName);
        }

        @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
        public void logAfterServiceMethodThrowing(JoinPoint joinPoint, Throwable exception) {
            String methodName = joinPoint.getSignature().getName();
            log.info("Service Method {} has thrown an Exception -> {}", methodName, exception.getMessage());
        }

        @Before("controllerMethods()")
        public void logBeforeControllerMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Controller Method {} is called with arguments: {}", methodName, methodArgs);
        }

        @AfterReturning(pointcut = "controllerMethods()", returning = "result")
        public void logAfterControllerMethodExecution(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Controller Method {} has returned.", methodName);
        }

        @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
        public void logAfterControllerMethodThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Controller Method {} has thrown an Exception -> {}", methodName, exception.getMessage());
    }
    }


