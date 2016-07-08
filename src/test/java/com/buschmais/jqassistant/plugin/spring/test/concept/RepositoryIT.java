package com.buschmais.jqassistant.plugin.spring.test.concept;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.components.AnnotatedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.RepositoryImpl;

public class RepositoryIT extends AbstractJavaPluginIT {
    
    @Test
    public void annotatedRepository() throws Exception {
        scanClasses(RepositoryImpl.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(SUCCESS));
       
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository:Component) RETURN r").getColumn("r"), hasItem(typeDescriptor(AnnotatedRepository.class)));
        store.commitTransaction();
    }

    @Test
    public void implementedRepository() throws Exception {
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(RepositoryImpl.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository:Component) RETURN r").getColumn("r"), hasItem(typeDescriptor(RepositoryImpl.class)));
        store.commitTransaction();
    }
    
    @Test
    public void repository() throws Exception {
        scanClasses(AnnotatedRepository.class,RepositoryImpl.class);
        assertThat(applyConcept("spring-data:Repository").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> repositories = query("MATCH (r:Spring:Repository:Component) RETURN r").getColumn("r");
        assertThat(repositories, hasItem(typeDescriptor(AnnotatedRepository.class)));
        assertThat(repositories, hasItem(typeDescriptor(RepositoryImpl.class)));
        store.commitTransaction();
    }

    private void clearConcepts() {
        store.beginTransaction();
        query("MATCH (c:Concept) DELETE c");
        store.commitTransaction();
    }    
}
