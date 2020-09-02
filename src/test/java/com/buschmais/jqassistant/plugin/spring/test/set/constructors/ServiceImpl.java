package com.buschmais.jqassistant.plugin.spring.test.set.constructors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl {

    private Repository repository;

    private Map<String, String> cache;

    private String value;

    @Autowired
    public ServiceImpl(RepositoryImpl repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void postConstruct() {
        this.cache = new ConcurrentHashMap<>();
    }

    @PreDestroy
    public void preDestroy() {
        this.cache = null;
    }

    @Bean(name = "cache")
    public Map<String,String> getCache() {
        return cache;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
