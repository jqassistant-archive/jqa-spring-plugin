package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestControllerWithServiceAndRepositoryDependency {

    private TestService1 testService1;

    private TestRepository1 testRepository1;

    @Autowired
    public TestControllerWithServiceAndRepositoryDependency(TestService1 testService1, TestRepository1 testRepository1) {
        this.testService1 = testService1;
        this.testRepository1 = testRepository1;
    }
}
