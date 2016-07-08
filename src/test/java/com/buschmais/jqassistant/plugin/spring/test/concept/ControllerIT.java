package com.buschmais.jqassistant.plugin.spring.test.concept;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.components.TestController;
import com.buschmais.jqassistant.plugin.spring.test.set.components.TestService;

public class ControllerIT extends AbstractJavaPluginIT {
    
    @Test
    public void controller() throws Exception {
        scanClasses(TestService.class);
        assertThat(applyConcept("spring-mvc:Controller").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(TestController.class);
        assertThat(applyConcept("spring-mvc:Controller").getStatus(), equalTo(SUCCESS));
       
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Controller:Component) RETURN r").getColumn("r"), hasItem(typeDescriptor(TestController.class)));
        store.commitTransaction();
    }

    private void clearConcepts() {
        store.beginTransaction();
        query("MATCH (c:Concept) DELETE c");
        store.commitTransaction();
    }
}
