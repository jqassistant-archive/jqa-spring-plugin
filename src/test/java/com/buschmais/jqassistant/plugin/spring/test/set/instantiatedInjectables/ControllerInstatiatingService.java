package com.buschmais.jqassistant.plugin.spring.test.set.instantiatedInjectables;

public class ControllerInstatiatingService {

    private void instantiateService(){
        TestService service = new TestService();
    }
    
}
