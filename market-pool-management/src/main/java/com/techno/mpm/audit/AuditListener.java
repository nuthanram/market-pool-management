package com.techno.mpm.audit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration
public class AuditListener {

	
	@Bean
	public AuditorAware<String> auditorAware() {
		return new EntityAuditorAware();
	}

}