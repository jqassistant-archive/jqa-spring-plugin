package com.buschmais.jqassistant.plugin.spring.test.concept;

import com.buschmais.jqassistant.plugin.spring.test.set.components.AnnotatedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.ImplementedRepository;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.*;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

class RepositoryIT extends AbstractSpringIT {

    @Test
    void annotatedRepository() throws Exception {
        scanClasses(ImplementedRepository.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(WARNING));
        clearConcepts();
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository) RETURN r").getColumn("r"), hasItem(typeDescriptor(AnnotatedRepository.class)));
        store.commitTransaction();
    }

    @Test
    void implementedRepository() throws Exception {
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(WARNING));
        clearConcepts();
        scanClasses(ImplementedRepository.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository) RETURN r").getColumn("r"), hasItem(typeDescriptor(ImplementedRepository.class)));
        store.commitTransaction();
    }
}
