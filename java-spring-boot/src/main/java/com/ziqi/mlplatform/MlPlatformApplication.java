package com.ziqi.mlplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MlPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MlPlatformApplication.class, args);
	}


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		String currentIp = System.getenv("CURRENT_IP");
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://"+currentIp+":3000")
						.allowedMethods("*")
						.allowedHeaders("*");
			}
		};
	}

}

