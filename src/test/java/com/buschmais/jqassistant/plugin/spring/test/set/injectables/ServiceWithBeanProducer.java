package com.buschmais.jqassistant.plugin.spring.test.set.injectables;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ServiceWithBeanProducer {

    @Bean
    public ConfigurationBean getBean() {
        return null;
    }

}
