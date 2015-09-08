package com.sam.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages="com.sam")
@EnableElasticsearchRepositories(basePackages = {"com.sam.repository"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

}
