package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestController1 {

    private TestService1 testService1;

    @Autowired
    public TestController1(TestService1 testService1) {
        this.testService1 = testService1;
    }
}
