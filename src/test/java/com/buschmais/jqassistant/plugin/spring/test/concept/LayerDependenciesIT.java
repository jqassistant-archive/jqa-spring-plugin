package com.buschmais.jqassistant.plugin.spring.test.concept;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository.ControllerWithServiceAndRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository.TestRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository.TestService;

public class LayerDependenciesIT extends AbstractJavaPluginIT {
    
    @Test
    public void layerDependencies() throws Exception {
        scanClasses(ControllerWithServiceAndRepository.class, TestRepository.class, TestService.class);
        assertThat(applyConcept("spring-mvc:Service").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("spring-mvc:Controller").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("spring-layer:VirtualComponentDependency").getStatus(), equalTo(SUCCESS));
       
        store.beginTransaction();
        assertThat(query("MATCH (r:Spring:Controller)-[:DEPENDS_ON] -> (s:Spring:Service) RETURN r").getColumn("r"), hasItem(typeDescriptor(ControllerWithServiceAndRepository.class)));
        store.commitTransaction();
    }

}
