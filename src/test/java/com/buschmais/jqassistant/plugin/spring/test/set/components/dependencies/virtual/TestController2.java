package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestController2 extends AbstractTestController implements TestController {

    private TestService testService;

    @Autowired
    public TestController2(TestService testService) {
        this.testService = testService;
    }
}
