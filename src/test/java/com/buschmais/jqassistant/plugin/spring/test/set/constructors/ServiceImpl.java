package com.buschmais.jqassistant.plugin.spring.test.set.constructors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl {

    private RepositoryImpl repository;

    private String value;

    @Autowired
    public ServiceImpl(RepositoryImpl repository) {
        this.repository = repository;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RepositoryImpl getRepository() {
        return repository;
    }

    public void setRepository(RepositoryImpl repository) {
        this.repository = repository;
    }
}
