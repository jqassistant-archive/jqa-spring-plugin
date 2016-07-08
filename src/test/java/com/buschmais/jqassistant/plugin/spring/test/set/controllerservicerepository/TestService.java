package com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    
    @Autowired
    private TestRepository repository;

}
