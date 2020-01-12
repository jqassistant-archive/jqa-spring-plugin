package com.buschmais.jqassistant.plugin.spring.test.set.injectables.subclass;

import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBean {

    @Bean
    public CustomizableTraceInterceptor methodTraceLoggingInterceptor() {
        return new SubClassOfInjectable();
    }

}
