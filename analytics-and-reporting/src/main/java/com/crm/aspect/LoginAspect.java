package com.crm.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoginAspect {

    @Before("execution (* com.crm.service.SalesPerformanceReportImpl.*(..)")
    public void log(){
        log.info("SERVICE CLASS METHOD CALLED");
    }

}
