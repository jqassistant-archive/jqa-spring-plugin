package com.buschmais.jqassistant.plugin.spring.test.set.controllerrepository.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ControllerWithRepositorInterface {

    @Autowired
    private RepositoryInterface repository;
    
}
