package com.bbms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankManagementSystemApplication {
	private static final Logger logger = LoggerFactory.getLogger(BankManagementSystemApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BankManagementSystemApplication.class, args);
		logger.info("Application started!");
	}
}
