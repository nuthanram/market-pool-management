package com.techno.mpm.audit;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class EntityAuditorAware extends BaseConfigController implements AuditorAware<String> {

	
	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(getUserId());
	}

}