package com.bookrentalsystem.bks.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class AuditingConfigUserDetail implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
         final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String auditor = "System";

        if (Objects.nonNull(authentication)) {
            auditor = authentication.getName();
        }

        return Optional.of(auditor);
    }
}
