package com.starter.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private Access access;
	
    private Refresh refresh;

    public static class Access {
        private String secret;
        
        private Duration expiration;

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public Duration getExpiration() {
			return expiration;
		}

		public void setExpiration(Duration expiration) {
			this.expiration = expiration;
		}
    }
    
    public static class Refresh {
        private String secret;
        
        private Duration expiration;

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public Duration getExpiration() {
			return expiration;
		}

		public void setExpiration(Duration expiration) {
			this.expiration = expiration;
		}
    }

	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

	public Refresh getRefresh() {
		return refresh;
	}

	public void setRefresh(Refresh refresh) {
		this.refresh = refresh;
	}
    
}
