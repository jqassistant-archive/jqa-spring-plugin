package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.stereotype.Service;

@Service
public class TestServiceWithInvalidDependencies {

    private TestController1 testController1;

    private TestConfiguration testConfiguration;

    public TestServiceWithInvalidDependencies(TestController1 testController1, TestConfiguration testConfiguration) {
        this.testController1 = testController1;
        this.testConfiguration = testConfiguration;
    }
}
