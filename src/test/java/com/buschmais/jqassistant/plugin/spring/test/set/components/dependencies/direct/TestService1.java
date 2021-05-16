package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService1 {

    private TestRepository1 testRepository1;

    private TestService2 testService2;

    private TestComponent testComponent;

    @Autowired
    public TestService1(TestRepository1 testRepository1, TestService2 testService2, TestComponent testComponent) {
        this.testRepository1 = testRepository1;
        this.testService2 = testService2;
        this.testComponent = testComponent;
    }
}
