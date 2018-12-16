package com.buschmais.jqassistant.plugin.spring.test.concept;

import com.buschmais.jqassistant.plugin.spring.test.set.components.AnnotatedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.ImplementedRepository;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class RepositoryIT extends AbstractSpringIT {

    @Test
    public void annotatedRepository() throws Exception {
        scanClasses(ImplementedRepository.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-data:AnnotatedRepository").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository) RETURN r").getColumn("r"), hasItem(typeDescriptor(AnnotatedRepository.class)));
        store.commitTransaction();
    }

    @Test
    public void implementedRepository() throws Exception {
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(ImplementedRepository.class);
        assertThat(applyConcept("spring-data:ImplementedRepository").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Repository) RETURN r").getColumn("r"), hasItem(typeDescriptor(ImplementedRepository.class)));
        store.commitTransaction();
    }
}
