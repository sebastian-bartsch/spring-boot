package com.tld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.tld")
@EnableJpaRepositories(basePackages ="com.tld.jpa.repository")
public class IotApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotApiApplication.class, args);
	}

}
