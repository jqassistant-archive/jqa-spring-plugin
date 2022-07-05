package com.buschmais.jqassistant.plugin.spring.test.concept;

import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.spring.test.set.components.ControllerAdvice;
import com.buschmais.jqassistant.plugin.spring.test.set.components.RestController;
import com.buschmais.jqassistant.plugin.spring.test.set.components.RestControllerAdvice;
import com.buschmais.jqassistant.plugin.spring.test.set.components.Service;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.*;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

class MvcIT extends AbstractSpringIT {

    @Test
    void restController() throws Exception {
        verify(RestController.class, "spring-mvc:RestController",
            ":Spring:RestController:Controller:Component:Injectable");
    }

    @Test
    void controllerAdvice() throws Exception {
        verify(ControllerAdvice.class, "spring-mvc:ControllerAdvice", ":Spring:ControllerAdvice:Component:Injectable");
    }

    @Test
    void restControllerAdvice() throws Exception {
        verify(RestControllerAdvice.class, "spring-mvc:RestControllerAdvice",
            ":Spring:RestControllerAdvice:ControllerAdvice:Component:Injectable");
    }

    private void verify(Class<?> componentType, String conceptId, String expectedLabels) throws RuleException {
        scanClasses(Service.class);
        assertThat(applyConcept(conceptId).getStatus(), equalTo(WARNING));
        clearConcepts();
        scanClasses(componentType);
        assertThat(applyConcept(conceptId).getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (c" + expectedLabels + ") RETURN c").getColumn("c"),
            hasItem(typeDescriptor(componentType)));
        store.commitTransaction();
    }
}
