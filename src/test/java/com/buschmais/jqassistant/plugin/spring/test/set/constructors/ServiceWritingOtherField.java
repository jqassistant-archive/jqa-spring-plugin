package com.buschmais.jqassistant.plugin.spring.test.set.constructors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceWritingOtherField {

    private String value;

    private String anotherValue;

    @Autowired
    public ServiceWritingOtherField(@Value("value") String value) {
        this.value = value;
    }

    public String getAnotherValue() {
        return anotherValue;
    }

    public void setAnotherValue(String anotherValue) {
        this.anotherValue = anotherValue;
    }
}
