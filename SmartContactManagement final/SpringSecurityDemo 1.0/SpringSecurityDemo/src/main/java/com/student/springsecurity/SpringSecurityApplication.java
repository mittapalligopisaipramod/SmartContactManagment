package com.student.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.main.SpringSecurityApplication.class, args);
	}

}

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
