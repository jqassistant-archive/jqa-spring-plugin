package com.buschmais.jqassistant.plugin.spring.test.set.interfaces;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class TestApplicationEventPublisherAwareBean implements ApplicationEventPublisherAware {

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    }
}
