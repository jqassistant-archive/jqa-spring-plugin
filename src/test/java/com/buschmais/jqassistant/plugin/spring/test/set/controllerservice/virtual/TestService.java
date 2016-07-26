package com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService implements ServiceInterface {
    
    @Autowired
    private TestRepository repository;

}
