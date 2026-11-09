package com.warehouse.warehouse_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Warehouse Management System.
 *
 * Serves as the entry point for the Spring Boot application.
 *
 * Enables Spring Boot auto-configuration, component scanning,
 * and application configuration through the
 * @SpringBootApplication annotation.
 *
 * Starts the application and initializes the Spring context,
 * allowing controllers, services, and repositories to work together.
 */
@SpringBootApplication
public class WarehouseManagementSystemApplication {

	/**
     * Starts the Warehouse Management System application.
     *
     * Initializes Spring Boot and starts the embedded web server,
     * making the REST API available for HTTP requests.
     *
     * @param args command-line arguments passed to the application
     */
	public static void main(String[] args) {
		SpringApplication.run(WarehouseManagementSystemApplication.class, args);
	}

}
