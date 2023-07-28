package com.techno.mpm;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
//@EnableJpaRepositories(basePackages = "com.techno.mpm.document.candidate")
//@EnableElasticsearchRepositories(basePackages = "com.techno.mpm.elastic.repository.candidate")
public class MarketPoolManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketPoolManagementApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
