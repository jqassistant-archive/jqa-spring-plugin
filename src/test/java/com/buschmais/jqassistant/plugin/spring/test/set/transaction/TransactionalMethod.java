package com.buschmais.jqassistant.plugin.spring.test.set.transaction;

import org.springframework.transaction.annotation.Transactional;

public class TransactionalMethod {

    @Transactional
    public void transactionalMethod(){
        
    }

    public void nonTransactionalMethod() {
    }

    private void callingTransactional() {
        transactionalMethod();
    }
    
}
