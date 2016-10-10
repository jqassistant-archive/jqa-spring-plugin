package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepositoryWithControllerDependency {

    private TestController1 testController1;

    @Autowired
    public TestRepositoryWithControllerDependency(TestController1 testController1) {
        this.testController1 = testController1;
    }
}
