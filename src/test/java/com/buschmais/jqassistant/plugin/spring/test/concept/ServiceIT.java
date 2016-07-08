package com.buschmais.jqassistant.plugin.spring.test.concept;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.components.AnnotatedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.TestService;

public class ServiceIT extends AbstractJavaPluginIT {
    
    @Test
    public void service() throws Exception {
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-mvc:Service").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(TestService.class);
        assertThat(applyConcept("spring-mvc:Service").getStatus(), equalTo(SUCCESS));
       
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Service) RETURN r").getColumn("r"), hasItem(typeDescriptor(TestService.class)));
        store.commitTransaction();
    }

    private void clearConcepts() {
        store.beginTransaction();
        query("MATCH (c:Concept) DELETE c");
        store.commitTransaction();
    }    
}
