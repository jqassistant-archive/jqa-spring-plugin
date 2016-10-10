package com.buschmais.jqassistant.plugin.spring.test.concept;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.buschmais.jqassistant.plugin.spring.test.set.components.TestAnnotatedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.TestImplementedRepository;

public class RepositoryIT extends AbstractSpringIT {
    
    @Test
    public void annotatedRepository() throws Exception {
        scanClasses(TestImplementedRepository.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(TestAnnotatedRepository.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(SUCCESS));
       
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository:Component) RETURN r").getColumn("r"), hasItem(typeDescriptor(TestAnnotatedRepository.class)));
        store.commitTransaction();
    }

    @Test
    public void implementedRepository() throws Exception {
        scanClasses(TestAnnotatedRepository.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(TestImplementedRepository.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository:Component) RETURN r").getColumn("r"), hasItem(typeDescriptor(TestImplementedRepository.class)));
        store.commitTransaction();
    }
}
