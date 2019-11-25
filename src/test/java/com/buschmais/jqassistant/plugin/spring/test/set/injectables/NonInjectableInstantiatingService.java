package com.buschmais.jqassistant.plugin.spring.test.set.injectables;

public class NonInjectableInstantiatingService {

    private void instantiateService(){
        Service service = new Service();
    }

}
