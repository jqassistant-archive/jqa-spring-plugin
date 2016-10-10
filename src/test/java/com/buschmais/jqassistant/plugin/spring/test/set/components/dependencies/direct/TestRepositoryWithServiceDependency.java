package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepositoryWithServiceDependency {

    private TestService1 testService1;

    @Autowired
    public TestRepositoryWithServiceDependency(TestService1 testService1) {
        this.testService1 = testService1;
    }
}
