package com.pws.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.pws.admin.utility.AuditAwareImpl;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.pws.admin.*"})
public class PwsAdminServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(PwsAdminServiceApplication.class, args);

	}
	@Bean
    public AuditorAware<String> auditorAware() {
        return new AuditAwareImpl();
    }


}
