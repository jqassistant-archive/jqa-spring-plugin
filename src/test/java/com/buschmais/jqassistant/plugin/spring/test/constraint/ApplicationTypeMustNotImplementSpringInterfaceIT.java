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
import com.buschmais.jqassistant.plugin.spring.test.set.interfaces.BusinessInterface;
import com.buschmais.jqassistant.plugin.spring.test.set.interfaces.ClassImplementingBusinessInterface;
import com.buschmais.jqassistant.plugin.spring.test.set.interfaces.ClassImplementingSpringInterface;

public class ApplicationTypeMustNotImplementSpringInterfaceIT extends AbstractJavaPluginIT {
    
    @Test
    public void typeImplementingBusinessInterface() throws Exception {
        scanClasses(ClassImplementingBusinessInterface.class, BusinessInterface.class);
        assertThat(validateConstraint("spring-injection:ApplicationTypeMustNotImplementSpringInterface").getStatus(), equalTo(SUCCESS));
    }
    
    @Test
    public void typeImplementingSpringInterface() throws Exception {
        scanClasses(ClassImplementingSpringInterface.class);
        assertThat(validateConstraint("spring-injection:ApplicationTypeMustNotImplementSpringInterface").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportWriter.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint("spring-injection:ApplicationTypeMustNotImplementSpringInterface")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        store.commitTransaction();
    }

}
