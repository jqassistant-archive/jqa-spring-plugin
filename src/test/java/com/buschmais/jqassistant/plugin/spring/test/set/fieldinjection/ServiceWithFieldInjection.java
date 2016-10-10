package com.buschmais.jqassistant.plugin.spring.test.set.fieldinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceWithFieldInjection {

    @Autowired
    public Repository repository;
}
