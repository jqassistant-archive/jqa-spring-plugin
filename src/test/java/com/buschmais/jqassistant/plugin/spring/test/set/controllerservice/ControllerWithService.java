package com.buschmais.jqassistant.plugin.spring.test.set.controllerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ControllerWithService {
    
    @Autowired
    private TestService service;

}
