package com.idaltchion.ifxmoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.idaltchion.ifxmoney.api.config.property.IfxmoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(IfxmoneyApiProperty.class) //Profile
@EnableScheduling //Schedule
public class IfxmoneyApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(IfxmoneyApiApplication.class, args);
	}
}
