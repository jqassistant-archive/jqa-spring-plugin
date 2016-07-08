package com.buschmais.jqassistant.plugin.spring.test.set.layerDependencies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService implements TestServiceInterface {
    
    @Autowired
    private TestRepository repository;

}
