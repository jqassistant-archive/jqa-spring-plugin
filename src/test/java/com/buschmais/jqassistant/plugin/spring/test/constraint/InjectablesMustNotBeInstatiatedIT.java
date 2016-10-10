package com.buschmais.jqassistant.plugin.spring.test.constraint;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.JavaClassesDirectoryDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ControllerInstantiatingService;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.TestService;

public class InjectablesMustNotBeInstatiatedIT extends AbstractJavaPluginIT {

    @Test
    public void applicationClasses() throws Exception {
        scanClasses("jar");
        assertThat(validateConstraint("spring-injection:InjectablesMustNotBeInstantiated").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportWriter.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint("spring-injection:InjectablesMustNotBeInstantiated")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        store.commitTransaction();
    }

    @Test
    public void testClasses() throws Exception {
        scanClasses("test-jar");
        assertThat(validateConstraint("spring-injection:InjectablesMustNotBeInstantiated").getStatus(), equalTo(SUCCESS));
    }

    private void scanClasses(String artifactType) throws IOException {
        scanClasses("a1", ControllerInstantiatingService.class, TestService.class);
        store.beginTransaction();
        JavaClassesDirectoryDescriptor a1 = getArtifactDescriptor("a1");
        a1.setType(artifactType);
        store.commitTransaction();
    }

}
