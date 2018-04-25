package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.components.dependencies.direct.*;

import org.junit.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class DependencyStructureIT extends AbstractJavaPluginIT {

    private static final String CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES = "spring-component:ControllerMustOnlyDependOnServicesRepositoriesOrComponents";
    	private static final String CONSTRAINT_ALLOWED_SERVICE_DEPENDENCIES = "spring-component:ServiceMustOnlyDependOnServicesRepositoriesOrComponents";
    	private static final String CONSTRAINT_ALLOWED_REPOSITORY_DEPENDENCIES = "spring-component:RepositoryMustOnlyDependOnRepositoriesOrComponents";


		@Test
    public void controllerDependsOnService() throws Exception {
        scanClasses(TestController1.class, TestService1.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void controllerDependsRepository() throws Exception {
        scanClasses(TestControllerWithRepositoryDependency.class, TestRepository1.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void controllerDependsOnController() throws Exception {
        scanClasses(TestControllerWithControllerDependency.class, TestController1.class);
        verifyConstraintViolation(CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES, "Controller", TestControllerWithControllerDependency.class,
                TestController1.class);
    }

    @Test
    public void controllerDependsOnServiceAndRepository() throws Exception {
        scanClasses(TestControllerWithServiceAndRepositoryDependency.class, TestService1.class, TestRepository1.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_CONTROLLER_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void serviceDependsOnServiceAndRepository() throws Exception {
        scanClasses(TestController1.class, TestService1.class, TestService2.class, TestRepository1.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_SERVICE_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void repositoryDependsOnRepository() throws Exception {
        scanClasses(TestRepository1.class, TestRepository2.class);
        assertThat(validateConstraint(CONSTRAINT_ALLOWED_REPOSITORY_DEPENDENCIES).getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void repositoryDependsOnController() throws Exception {
        scanClasses(TestRepositoryWithControllerDependency.class, TestController1.class);
        verifyConstraintViolation(CONSTRAINT_ALLOWED_REPOSITORY_DEPENDENCIES, "Repository", TestRepositoryWithControllerDependency.class,
                TestController1.class);
    }

    @Test
    public void repositoryDependsOnService() throws Exception {
        scanClasses(TestRepositoryWithServiceDependency.class, TestService1.class);
        verifyConstraintViolation(CONSTRAINT_ALLOWED_REPOSITORY_DEPENDENCIES, "Repository", TestRepositoryWithServiceDependency.class,
                TestService1.class);
    }

    private void verifyConstraintViolation(String constraintId, String componentColumn, Class<?> component, Class<?>... dependencies) throws Exception {
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
        assertThat(invalidDependencies.size(), equalTo(dependencies.length));
        for (Class<?> dependency : dependencies) {
            assertThat(invalidDependencies, hasItem(typeDescriptor(dependency)));
        }
        store.commitTransaction();
    }

}
