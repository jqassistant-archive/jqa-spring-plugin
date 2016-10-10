package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestControllerWithRepositoryDependency {

    private TestRepository1 testRepository1;

    @Autowired
    public TestControllerWithRepositoryDependency(TestRepository1 testRepository1) {
        this.testRepository1 = testRepository1;
    }
}
