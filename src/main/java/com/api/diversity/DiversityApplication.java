package com.api.diversity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DiversityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiversityApplication.class, args);
	}

}
