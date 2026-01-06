package com.smartradiology.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmartRadiologyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartRadiologyApplication.class, args);
	}

}
