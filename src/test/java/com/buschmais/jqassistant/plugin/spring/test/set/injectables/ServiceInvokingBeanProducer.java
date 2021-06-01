package com.buschmais.jqassistant.plugin.spring.test.set.injectables;

public class ServiceInvokingBeanProducer {

    private final ConfigurationWithBeanProducer configurationWithBeanProducer;

    public ServiceInvokingBeanProducer(ConfigurationWithBeanProducer configurationWithBeanProducer) {
        this.configurationWithBeanProducer = configurationWithBeanProducer;
    }

    public void doSomething() {
        configurationWithBeanProducer.getConfiguration();
    }
}
