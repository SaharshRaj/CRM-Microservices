package com.crm;

import com.crm.controller.SalesOpportunityController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SalesAutomationApplicationTests {

	@Autowired
	SalesOpportunityController controller;

	@Test
	void contextLoads() {
		assertNotNull(controller);
	}

	@Test
	void main() {
		SalesAutomationApplication.main(new String[] {});
		assertNotNull(controller);
	}

}
