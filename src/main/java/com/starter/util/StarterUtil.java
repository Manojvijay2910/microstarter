package com.starter.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class StarterUtil {

	public static UserDetails getUserDetail() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null
	            || !authentication.isAuthenticated()
	            || authentication.getPrincipal() instanceof String) {
	        return null;
	    }

		return (UserDetails) authentication.getPrincipal();
	}
}
