package com.buschmais.jqassistant.plugin.spring.test.concept;

import com.buschmais.jqassistant.plugin.spring.test.set.components.RestController;
import com.buschmais.jqassistant.plugin.spring.test.set.components.Service;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class MvcIT extends AbstractSpringIT {

    @Test
    public void restController() throws Exception {
        scanClasses(Service.class);
        assertThat(applyConcept("spring-mvc:RestController").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(RestController.class);
        assertThat(applyConcept("spring-mvc:RestController").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (c:Spring:RestController:Controller) RETURN c").getColumn("c"), hasItem(typeDescriptor(RestController.class)));
        store.commitTransaction();
    }

}
