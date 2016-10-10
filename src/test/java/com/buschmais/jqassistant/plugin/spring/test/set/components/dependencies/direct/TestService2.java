package com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService2 {

    private TestRepository2 testRepository2;

    @Autowired
    public TestService2(TestRepository2 testRepository2) {
        this.testRepository2 = testRepository2;
    }
}
