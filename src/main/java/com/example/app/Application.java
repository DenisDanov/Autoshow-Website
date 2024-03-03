package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.example.app.data")
@ComponentScan(basePackages = "com.example.app.services")
@ComponentScan(basePackages = "com.example.app.services.impl")
@ComponentScan(basePackages = "com.example.app.data.entities")
@ComponentScan(basePackages = "com.example.app.data.repositories")
@ComponentScan(basePackages = "com.example.app.data.DTOs")
@ComponentScan(basePackages = "com.example.app.configs")
@ComponentScan(basePackages = "com.example.app.controllers")
@ComponentScan(basePackages = "com.example.app.controllers.utils")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
