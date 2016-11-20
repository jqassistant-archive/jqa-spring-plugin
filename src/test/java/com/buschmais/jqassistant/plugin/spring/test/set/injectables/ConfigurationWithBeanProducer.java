package com.buschmais.jqassistant.plugin.spring.test.set.injectables;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationWithBeanProducer {

    @Bean
    public ConfigurationBean getConfiguration() {
        return new ConfigurationBean();
    };

}
