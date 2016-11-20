package com.buschmais.jqassistant.plugin.spring.test.set.interfaces;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class TestInitializingBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
    }

}
