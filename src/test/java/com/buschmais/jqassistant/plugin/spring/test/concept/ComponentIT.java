package com.buschmais.jqassistant.plugin.spring.test.concept;

import java.util.List;

import com.buschmais.jqassistant.plugin.spring.test.set.components.AnnotatedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.Controller;
import com.buschmais.jqassistant.plugin.spring.test.set.components.ImplementedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.Service;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.TestRepository1;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.TestService1;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ConfigurationWithBeanProducer;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class ComponentIT extends AbstractSpringIT {

    @Test
    public void configuration() throws Exception {
        scanClasses(ConfigurationWithBeanProducer.class);
        assertThat(applyConcept("spring-component:Configuration").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> configurations = query("MATCH (c:Spring:Configuration) RETURN c").getColumn("c");
        assertThat(configurations, hasItem(typeDescriptor(ConfigurationWithBeanProducer.class)));
        store.commitTransaction();
    }

    @Test
    public void controller() throws Exception {
        scanClasses(Service.class);
        assertThat(applyConcept("spring-component:Controller").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(Controller.class);
        assertThat(applyConcept("spring-component:Controller").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (c:Spring:Controller) RETURN c").getColumn("c"), hasItem(typeDescriptor(Controller.class)));
        store.commitTransaction();
    }

    @Test
    public void service() throws Exception {
        scanClasses(AnnotatedRepository.class);
        assertThat(applyConcept("spring-component:Service").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(Service.class);
        assertThat(applyConcept("spring-component:Service").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();
        assertThat(query("MATCH (s:Spring:Service) RETURN s").getColumn("s"), hasItem(typeDescriptor(Service.class)));
        store.commitTransaction();
    }

    @Test
    public void repository() throws Exception {
        scanClasses(AnnotatedRepository.class, ImplementedRepository.class);
        assertThat(applyConcept("spring-component:Repository").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> repositories = query("MATCH (r:Spring:Repository) RETURN r").getColumn("r");
        assertThat(repositories, hasItem(typeDescriptor(AnnotatedRepository.class)));
        assertThat(repositories, hasItem(typeDescriptor(ImplementedRepository.class)));
        store.commitTransaction();
    }

    @Test
    public void directComponentDependencies() throws Exception {
        scanClasses(com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.TestController1.class, TestService1.class,
                TestRepository1.class);
        assertThat(applyConcept("spring-component:Controller").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("spring-component:Service").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("spring-component:Repository").getStatus(), equalTo(SUCCESS));
        verifyComponentDependencies("MATCH (:Spring:Controller)-[:DEPENDS_ON]->(c:Spring:Injectable) RETURN c", TestService1.class);
        verifyComponentDependencies("MATCH (:Spring:Service)-[:DEPENDS_ON]->(c:Spring:Injectable) RETURN c", TestRepository1.class);
    }

    private void verifyComponentDependencies(String query, Class<?>... dependencies) {
        store.beginTransaction();
        TestResult result = query(query);
        assertThat(result.getRows().size(), equalTo(dependencies.length));
        List<Object> components = result.getColumn("c");
        for (Class<?> dependency : dependencies) {
            assertThat(components, hasItem(typeDescriptor(dependency)));
        }
        store.commitTransaction();
    }
}
