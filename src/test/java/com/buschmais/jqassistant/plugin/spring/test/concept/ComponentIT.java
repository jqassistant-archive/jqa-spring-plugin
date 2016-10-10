package com.buschmais.jqassistant.plugin.spring.test.concept;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.buschmais.jqassistant.plugin.spring.test.set.components.TestAnnotatedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.TestImplementedRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.TestController1;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.TestRepository1;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.TestService1;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.virtual.*;

public class ComponentIT extends AbstractSpringIT {

    @Test
    public void controller() throws Exception {
        scanClasses(com.buschmais.jqassistant.plugin.spring.test.set.components.TestService.class);
        assertThat(applyConcept("spring-component:Controller").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(com.buschmais.jqassistant.plugin.spring.test.set.components.TestController.class);
        assertThat(applyConcept("spring-component:Controller").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();
        assertThat(query("MATCH (c:Spring:Controller:Component) RETURN c").getColumn("c"),
                hasItem(typeDescriptor(com.buschmais.jqassistant.plugin.spring.test.set.components.TestController.class)));
        store.commitTransaction();
    }

    @Test
    public void service() throws Exception {
        scanClasses(TestAnnotatedRepository.class);
        assertThat(applyConcept("spring-component:Service").getStatus(), equalTo(FAILURE));
        clearConcepts();
        scanClasses(com.buschmais.jqassistant.plugin.spring.test.set.components.TestService.class);
        assertThat(applyConcept("spring-component:Service").getStatus(), equalTo(SUCCESS));

        store.beginTransaction();
        assertThat(query("MATCH (s:Spring:Service:Component) RETURN s").getColumn("s"),
                hasItem(typeDescriptor(com.buschmais.jqassistant.plugin.spring.test.set.components.TestService.class)));
        store.commitTransaction();
    }

    @Test
    public void repository() throws Exception {
        scanClasses(TestAnnotatedRepository.class, TestImplementedRepository.class);
        assertThat(applyConcept("spring-component:Repository").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> repositories = query("MATCH (r:Spring:Repository:Component) RETURN r").getColumn("r");
        assertThat(repositories, hasItem(typeDescriptor(TestAnnotatedRepository.class)));
        assertThat(repositories, hasItem(typeDescriptor(TestImplementedRepository.class)));
        store.commitTransaction();
    }

    @Test
    public void directComponentDependencies() throws Exception {
        scanClasses(TestController1.class, TestService1.class, TestRepository1.class);
        assertThat(applyConcept("spring-component:Controller").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("spring-component:Service").getStatus(), equalTo(SUCCESS));
        assertThat(applyConcept("spring-component:Repository").getStatus(), equalTo(SUCCESS));
        verifyComponentDependencies("MATCH (:Spring:Controller)-[:DEPENDS_ON]->(c:Spring:Component) RETURN c", TestService1.class);
        verifyComponentDependencies("MATCH (:Spring:Service)-[:DEPENDS_ON]->(c:Spring:Component) RETURN c", TestRepository1.class);
    }

    @Test
    public void virtualComponentDependencies() throws Exception {
        scanClasses(TestController.class, TestService.class, TestServiceImpl.class, TestRepository.class, TestRepositoryImpl.class);
        assertThat(applyConcept("spring-component:VirtualDependency").getStatus(), equalTo(SUCCESS));
        verifyComponentDependencies("MATCH (:Spring:Controller)-[:DEPENDS_ON{virtual:true}]->(c:Spring:Component) RETURN c", TestServiceImpl.class);
        verifyComponentDependencies("MATCH (:Spring:Service)-[:DEPENDS_ON{virtual:true}]->(c:Spring:Component) RETURN c", TestRepositoryImpl.class);
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
