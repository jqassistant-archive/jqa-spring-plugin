package com.buschmais.jqassistant.plugin.spring.test.set.transactionMethods;

import org.springframework.transaction.annotation.Transactional;

public class TransactionalClass {

    @Transactional
    private void transactionalMethod(){
        
    }
    
    private void callingTransactional() {
        transactionalMethod();
    }
    
}
