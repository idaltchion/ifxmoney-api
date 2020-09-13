package com.idaltchion.ifxmoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.idaltchion.ifxmoney.api.config.property.IfxmoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(IfxmoneyApiProperty.class) //Profile
@EnableScheduling //Schedule
public class IfxmoneyApiApplication {
	
	private static ApplicationContext APPLICATION_CONTEXT;
	
	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(IfxmoneyApiApplication.class, args);
	}

	//utilizado para pegar a instancia de uma classe. Utilizando para pegar a classe S3 no LancamentoAnexoListener
	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
}
