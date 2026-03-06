package com.doban.cadastro_pessoas_docs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Profile("dev")
@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeClientInfo(false);
        filter.setIncludeHeaders(false);
        filter.setIncludePayload(false);
        filter.setMaxPayloadLength(1000);
        filter.setBeforeMessagePrefix("REQUEST: ");
        filter.setAfterMessagePrefix("");
        filter.setAfterMessageSuffix("");
        return filter;
    }
}
