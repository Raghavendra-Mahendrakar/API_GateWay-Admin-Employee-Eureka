package com.pws.admin.utility;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        if (auth != null) {
            username = auth.getName();
        } else {
            username = "admin";
        }
        return Optional.of(username);
    }

    @Bean
    public AuditorAware<String> createAuditorProvider() {
        return new AuditAwareImpl();
    }

    @Bean
    public AuditingEntityListener createAuditingListener() {
        return new AuditingEntityListener();
    }

}
