package com.buschmais.jqassistant.plugin.spring.test.set.application.valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.buschmais.jqassistant.plugin.spring.test.set.application.valid.service.Service;

@org.springframework.stereotype.Controller
public class Controller {

    private Service service;

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }
}
