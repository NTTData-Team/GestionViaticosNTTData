package com.nttdata.empleado_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.nttdata")
@EnableDiscoveryClient
public class EmpleadoMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpleadoMsApplication.class, args);
	}

}
