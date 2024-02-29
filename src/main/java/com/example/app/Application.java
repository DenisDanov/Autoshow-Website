package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.example.app.dbData")
@ComponentScan(basePackages = "com.example.app.dbData.unsuccessfulLoginAttempts")
@ComponentScan(basePackages = "com.example.app.emailApp")
@ComponentScan(basePackages = "com.example.app.emailApp.newsLetterEmails")
@ComponentScan(basePackages = "com.example.app.dbData.changeUserDetails")
@ComponentScan(basePackages = "com.example.app.dbData.orderCarService")
@ComponentScan(basePackages = "com.example.app.dbData.recentlyViewedToken")
@ComponentScan(basePackages = "com.example.app.dbData.ReplacedTokens")
@ComponentScan(basePackages = "com.example.app.dbData.SecurityConfigs")
@ComponentScan(basePackages = "com.example.app.api")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
