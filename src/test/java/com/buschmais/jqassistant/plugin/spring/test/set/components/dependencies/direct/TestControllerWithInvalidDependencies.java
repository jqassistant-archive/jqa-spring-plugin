package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestControllerWithInvalidDependencies {

    private TestController1 testController1;

    private TestConfiguration testConfiguration;

    @Autowired
    public TestControllerWithInvalidDependencies(TestController1 testController1, TestConfiguration testConfiguration) {
        this.testController1 = testController1;
        this.testConfiguration = testConfiguration;
    }
}
