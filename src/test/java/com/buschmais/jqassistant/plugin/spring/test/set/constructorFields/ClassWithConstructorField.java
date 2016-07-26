package com.buschmais.jqassistant.plugin.spring.test.set.constructorFields;

public class ClassWithConstructorField {

    private String string;
    
    private ClassWithConstructorField() {
        this.string = "String";
    }
    
    private void manipulatingString() {
        this.string = "New String";
    }
    
}
