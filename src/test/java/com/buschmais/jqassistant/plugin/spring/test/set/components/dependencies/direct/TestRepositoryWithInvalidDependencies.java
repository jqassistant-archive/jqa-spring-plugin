package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepositoryWithInvalidDependencies {

    private TestRepository1 testRepository1;

    private TestService1 testService1;

    private TestController1 testController1;

    private TestConfiguration testConfiguration;

    @Autowired
    public TestRepositoryWithInvalidDependencies(TestRepository1 testRepository1, TestService1 testService1, TestController1 testController1,
            TestConfiguration testConfiguration) {
        this.testRepository1 = testRepository1;
        this.testService1 = testService1;
        this.testController1 = testController1;
        this.testConfiguration = testConfiguration;
    }
}
