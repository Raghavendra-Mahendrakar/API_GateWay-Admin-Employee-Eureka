package com.pws.employee;

import com.pws.employee.controller.EmployeeController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.pws.employee.utility.AuditAwareImpl;
import org.springframework.web.client.RestClientException;

import java.io.IOException;


@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.pws.employee.*"})

public class EmployeeApplication {

	public static void main(String[] args) throws RestClientException, IOException {
		ApplicationContext ctx = SpringApplication.run(
				EmployeeApplication.class, args);

		EmployeeController consumerControllerClient=ctx.getBean(EmployeeController.class);
		System.out.println(consumerControllerClient);
		consumerControllerClient.getAdmin();

	}
	@Bean
   public AuditorAware<String> auditorAware() {
        return new AuditAwareImpl();
   }
}

