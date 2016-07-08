package com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ControllerWithServiceAndRepository {
    
    @Autowired
    private TestRepository repository;
    
    @Autowired
    private TestService service;

}
