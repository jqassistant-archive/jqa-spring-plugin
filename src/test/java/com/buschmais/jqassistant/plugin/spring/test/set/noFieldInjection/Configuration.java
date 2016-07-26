package com.buschmais.jqassistant.plugin.spring.test.set.noFieldInjection;

import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    public String test;
}
