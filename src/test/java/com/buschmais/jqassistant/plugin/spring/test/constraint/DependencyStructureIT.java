package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.*;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

class DependencyStructureIT extends AbstractJavaPluginIT {

    private static final String CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES = "spring-component:ControllerMustOnlyDependOnServicesRepositories";
    private static final String CONSTRAINT_ALLOWED_SERVICE_DEPENDENCIES = "spring-component:ServiceMustOnlyDependOnServicesRepositories";
    private static final String CONSTRAINT_ALLOWED_REPOSITORY_DEPENDENCIES = "spring-component:RepositoryMustOnlyDependOnRepositories";

    @Test
    void validControllerDependencies() throws Exception {
        scanClasses(TestController1.class, TestService1.class, TestRepository1.class, TestConfiguration.class, TestComponent.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    void invalidControllerDependencies() throws Exception {
        scanClasses(TestControllerWithInvalidDependencies.class, TestController1.class, TestConfiguration.class);
        verifyConstraintViolation(CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES, "Controller", TestControllerWithInvalidDependencies.class, TestController1.class,
                TestConfiguration.class);
    }

    @Test
    void validServiceDependencies() throws Exception {
        scanClasses(TestController1.class, TestService1.class, TestService2.class, TestRepository1.class, TestConfiguration.class, TestComponent.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_SERVICE_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    void invalidServiceDependencies() throws Exception {
        scanClasses(TestServiceWithInvalidDependencies.class, TestRepository1.class, TestService1.class, TestController1.class, TestConfiguration.class);
        verifyConstraintViolation(CONSTRAINT_ALLOWED_SERVICE_DEPENDENCIES, "Service", TestServiceWithInvalidDependencies.class, TestController1.class,
                TestConfiguration.class);
    }

    @Test
    void validRepositoryDependencies() throws Exception {
        scanClasses(TestRepository1.class, TestRepository2.class, TestConfiguration.class, TestComponent.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_REPOSITORY_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    void invalidRepositoryDependencies() throws Exception {
        scanClasses(TestRepositoryWithInvalidDependencies.class, TestRepository1.class, TestService1.class, TestController1.class, TestConfiguration.class);
        verifyConstraintViolation(CONSTRAINT_ALLOWED_REPOSITORY_DEPENDENCIES, "Repository", TestRepositoryWithInvalidDependencies.class, TestService1.class,
                TestController1.class, TestConfiguration.class);
    }

    private void verifyConstraintViolation(String constraintId, String componentColumn, Class<?> component, Class<?>... expectedInvalidDependencies)
            throws Exception {
        assertThat(validateConstraint(constraintId).getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint(constraintId)));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        TypeDescriptor repository = (TypeDescriptor) row.get(componentColumn);
        assertThat(repository, typeDescriptor(component));
        List<TypeDescriptor> invalidDependencies = (List<TypeDescriptor>) row.get("InvalidDependencies");
        assertThat(invalidDependencies.size(), equalTo(expectedInvalidDependencies.length));
        for (Class<?> dependency : expectedInvalidDependencies) {
            assertThat(invalidDependencies, hasItem(typeDescriptor(dependency)));
        }
        store.commitTransaction();
    }

}
