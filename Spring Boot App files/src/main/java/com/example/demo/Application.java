package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo.dbData")
@ComponentScan(basePackages = "com.example.demo.dbData.unsuccessfulLoginAttempts")
@ComponentScan(basePackages = "com.example.demo.emailApp")
@ComponentScan(basePackages = "com.example.demo.emailApp.newsLetterEmails")
@ComponentScan(basePackages = "com.example.demo.dbData.changeUserDetails")
@ComponentScan(basePackages = "com.example.demo.dbData.orderCarService")
@ComponentScan(basePackages = "com.example.demo.dbData.recentlyViewedToken")
@ComponentScan(basePackages = "com.example.demo.dbData.ReplacedTokens")
@ComponentScan(basePackages = "com.example.demo.dbData.SecurityConfigs")
@ComponentScan(basePackages = "com.example.demo.api")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
