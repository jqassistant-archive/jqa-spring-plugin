package com.buschmais.jqassistant.plugin.spring.test.set.constructors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceWritingConstructorField {

    private String value;

    @Autowired
    public ServiceWritingConstructorField(@Value("value") String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
