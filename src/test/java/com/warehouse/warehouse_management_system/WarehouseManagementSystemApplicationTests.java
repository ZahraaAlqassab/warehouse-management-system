package com.warehouse.warehouse_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test for the Warehouse Management System application.
 *
 * Verifies that the Spring Boot application context loads
 * successfully without configuration or initialization errors.
 *
 * Uses @SpringBootTest to start the Spring application context,
 * including its configured components and dependencies.
 *
 * Helps ensure that the application can start correctly.
 */
@SpringBootTest
class WarehouseManagementSystemApplicationTests {

	 /**
     * Tests that the Spring Boot application context loads successfully.
     *
     * The test passes if Spring initializes the application context
     * without throwing an exception.
     */
	@Test
	void contextLoads() {
	}
}