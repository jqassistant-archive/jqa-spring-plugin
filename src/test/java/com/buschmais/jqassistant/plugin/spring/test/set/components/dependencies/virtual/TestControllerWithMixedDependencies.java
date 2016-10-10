package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.virtual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestControllerWithMixedDependencies {

    private TestService testService;

    private TestRepository testRepository;

    @Autowired
    public TestControllerWithMixedDependencies(TestService testService, TestRepository testRepository) {
        this.testService = testService;
        this.testRepository = testRepository;
    }
}
