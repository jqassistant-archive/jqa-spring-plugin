package com.buschmais.jqassistant.plugin.spring.test.set.injectables;

import org.springframework.stereotype.Controller;

@Controller
public class ControllerInstantiatingService {

    private final GeneratedControllerInstantiatingService generatedService;

    public ControllerInstantiatingService(GeneratedControllerInstantiatingService generatedService) {
        this.generatedService = generatedService;
    }

    private void instantiateService(){
        Service service = new Service();
    }

    private void instantiateServiceThroughGeneratedClass() {
        generatedService.instantiateService();
    }
}
