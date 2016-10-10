package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestControllerWithControllerDependency {

    private TestController1 testController1;

    @Autowired
    public TestControllerWithControllerDependency(TestController1 testController1) {
        this.testController1 = testController1;
    }
}
