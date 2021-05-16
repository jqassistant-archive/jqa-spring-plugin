package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepository1 {

    private TestRepository2 testRepository2;

    private TestComponent testComponent;

    @Autowired
    public TestRepository1(TestRepository2 testRepository2, TestComponent testComponent) {
        this.testRepository2 = testRepository2;
        this.testComponent = testComponent;
    }
}
