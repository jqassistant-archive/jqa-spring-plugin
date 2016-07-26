package com.buschmais.jqassistant.plugin.spring.test.constraint;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.layerDependencies.ControllerWithLayerDependencies;
import com.buschmais.jqassistant.plugin.spring.test.set.layerDependencies.TestRepository;
import com.buschmais.jqassistant.plugin.spring.test.set.layerDependencies.TestService;
import com.buschmais.jqassistant.plugin.spring.test.set.layerDependencies.TestServiceInterface;
import com.buschmais.jqassistant.plugin.spring.test.set.layerDependencies.app.Application;

public class NoTypesOutsideApplicationPackageIT extends AbstractJavaPluginIT  {
    
    @Test
    @Ignore
    public void noTypesOutsideApplicationPackage() throws Exception {        
        scanClasses(Application.class, ControllerWithLayerDependencies.class, TestRepository.class, TestService.class,TestServiceInterface.class );
        assertThat(validateConstraint("spring-boot:NoTypesOutsideApplicationPackage").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportWriter.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint("spring-boot:NoTypesOutsideApplicationPackage")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        store.commitTransaction();
        
    }
}
