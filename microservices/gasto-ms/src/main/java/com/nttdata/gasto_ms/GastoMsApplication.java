package com.nttdata.gasto_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.nttdata")
public class GastoMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GastoMsApplication.class, args);
	}

}
