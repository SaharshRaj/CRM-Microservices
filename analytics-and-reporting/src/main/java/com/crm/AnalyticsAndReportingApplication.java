package com.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AnalyticsAndReportingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsAndReportingApplication.class, args);
	}

}
