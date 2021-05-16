package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestController1 {

    private TestService1 testService1;

    private TestRepository1 testRepository1;

    private TestComponent testComponent;

    @Autowired
    public TestController1(TestService1 testService1, TestRepository1 testRepository1, TestComponent testComponent) {
        this.testService1 = testService1;
        this.testRepository1 = testRepository1;
        this.testComponent = testComponent;
    }
}
