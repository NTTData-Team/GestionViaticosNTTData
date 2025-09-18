package com.nttdata.viatico_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.nttdata")
public class ViaticoMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViaticoMsApplication.class, args);
	}

}
