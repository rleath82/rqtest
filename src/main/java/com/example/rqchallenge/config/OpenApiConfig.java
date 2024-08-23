package com.example.rqchallenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	@Value("${swagger.application.base.package}")
	private String basePackageName;
	@Value("${swagger.application.display.name}")
	private String applicationDisplayName;
	@Value("${swagger.application.description}")
	private String applicationDescription;
	
	@Bean
	OpenAPI myOpenAPI() {
		Info info = new Info()
				.title(applicationDisplayName)
				.description(applicationDescription);
		return new OpenAPI().info(info);
	}
}
