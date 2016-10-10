package com.buschmais.jqassistant.plugin.spring.test.set.application.valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.buschmais.jqassistant.plugin.spring.test.set.application.valid.service.TestService;

@Controller
public class TestController {

    private TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }
}
