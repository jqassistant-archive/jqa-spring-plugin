package com.buschmais.jqassistant.plugin.spring.test.set.layerDependencies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class ControllerWithLayerDependencies {
    
    @Autowired
    private TestRepository repository;
    
    @Autowired
    private TestServiceInterface service;
    
    @Transactional
    private void transactionMethod(){
        
    }

    private void callingMethod() {
        transactionMethod();
    }
    
}
