package com.buschmais.jqassistant.plugin.spring.test.set.fieldinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceWithConstructorInjection {

    private Repository repository;

    @Autowired
    public ServiceWithConstructorInjection(Repository repository) {
        this.repository = repository;
    }
}
