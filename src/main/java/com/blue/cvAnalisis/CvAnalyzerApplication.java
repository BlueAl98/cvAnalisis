package com.blue.cvAnalisis;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class CvAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CvAnalyzerApplication.class, args);
	}

}
