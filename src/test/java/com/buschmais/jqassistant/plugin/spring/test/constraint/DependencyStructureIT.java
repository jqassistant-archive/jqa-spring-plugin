package com.buschmais.jqassistant.plugin.spring.test.constraint;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.*;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerrepository.direct.ControllerWithRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerrepository.virtual.ControllerWithRepositorInterface;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerrepository.virtual.RepositoryInterface;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.direct.ControllerWithService;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.virtual.ControllerWithServiceInterface;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.virtual.ServiceInterface;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository.ControllerWithServiceAndRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository.TestRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.controllerservicerepository.TestService;

public class DependencyStructureIT extends AbstractJavaPluginIT {

    @Test
    public void dependencyStructure() throws Exception {
        scanClasses(ControllerWithServiceAndRepository.class, TestService.class, TestRepository.class);
        assertThat(validateConstraint("spring-layer:ControllerMustNotDependOnRepositoryUsedByService").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportWriter.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint("spring-layer:ControllerMustNotDependOnRepositoryUsedByService")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        store.commitTransaction();
    }

    @Test
    public void dependencyStructureRepository() throws Exception {
        scanClasses(ControllerWithRepository.class, com.buschmais.jqassistant.plugin.spring.test.set.controllerrepository.direct.TestRepository.class);
        assertThat(validateConstraint("spring-layer:ControllerMustNotDependOnRepositoryUsedByService").getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void dependencyStructureRepositoryVirtual() throws Exception {
        scanClasses(ControllerWithRepositorInterface.class, RepositoryInterface.class, com.buschmais.jqassistant.plugin.spring.test.set.controllerrepository.virtual.TestRepository.class);
        assertThat(validateConstraint("spring-layer:ControllerMustNotDependOnRepositoryUsedByService").getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void dependencyStructureService() throws Exception {
        scanClasses(ControllerWithService.class, com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.direct.TestService.class, com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.direct.TestRepository.class);
        assertThat(validateConstraint("spring-layer:ControllerMustNotDependOnRepositoryUsedByService").getStatus(), equalTo(SUCCESS));
    }
    @Test
    public void dependencyStructureServiceVirtual() throws Exception {
        scanClasses(ControllerWithServiceInterface.class, com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.virtual.TestService.class, com.buschmais.jqassistant.plugin.spring.test.set.controllerservice.virtual.TestRepository.class, ServiceInterface.class);
        assertThat(validateConstraint("spring-layer:ControllerMustNotDependOnRepositoryUsedByService").getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void dependencyStructureStrong() throws Exception {
        scanClasses(ControllerWithServiceAndRepository.class, TestService.class, TestRepository.class);
        assertThat(validateConstraint("spring-layer:ControllerMustDependEitherOnServicesOrRepositories").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportWriter.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint("spring-layer:ControllerMustDependEitherOnServicesOrRepositories")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        store.commitTransaction();
    }
}
