package com.exercise.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * main application for the system
 * @author wby
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

}
