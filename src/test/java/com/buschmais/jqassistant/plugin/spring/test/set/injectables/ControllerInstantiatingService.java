package com.buschmais.jqassistant.plugin.spring.test.set.injectables;

import org.springframework.stereotype.Controller;

@Controller
public class ControllerInstantiatingService {

    private void instantiateService(){
        Service service = new Service();
    }
    
}
