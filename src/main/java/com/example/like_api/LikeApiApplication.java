package com.example.like_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LikeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LikeApiApplication.class, args);
	}

}
