package com.starter.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.starter.util.StarterUtil;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {

	    var userDetail = StarterUtil.getUserDetail();

	    if (userDetail == null) {
	        return Optional.of("SYSTEM");
	    }

	    return Optional.ofNullable(userDetail.getUsername());
	}
}
