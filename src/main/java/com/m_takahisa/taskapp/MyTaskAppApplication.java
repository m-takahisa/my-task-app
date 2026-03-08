package com.m_takahisa.taskapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyTaskAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTaskAppApplication.class, args);
	}

}
