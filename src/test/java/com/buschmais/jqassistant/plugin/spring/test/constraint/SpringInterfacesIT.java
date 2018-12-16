package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.interfaces.*;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SpringInterfacesIT extends AbstractJavaPluginIT {

    @Test
    public void avoidInitializingBean() throws Exception {
        verifyConstraint(TestInitializingBean.class, "spring-injection:AvoidInitializingBean");
    }

    @Test
    public void avoidDisposableBean() throws Exception {
        verifyConstraint(TestDisposableBean.class, "spring-injection:AvoidDisposableBean");
    }

    @Test
    public void avoidBeanFactoryAware() throws Exception {
        verifyConstraint(TestBeanFactoryAwareBean.class, "spring-injection:AvoidAwareInterfacesInFavorOfInjection");
    }

    @Test
    public void avoidApplicationContextAware() throws Exception {
        verifyConstraint(TestApplicationContextAwareBean.class, "spring-injection:AvoidAwareInterfacesInFavorOfInjection");
    }

    @Test
    public void avoidApplicationEventPublisherAwareBean() throws Exception {
        verifyConstraint(TestApplicationEventPublisherAwareBean.class, "spring-injection:AvoidAwareInterfacesInFavorOfInjection");
    }

    private void verifyConstraint(Class<?> type, String constraintId) throws Exception {
        scanClasses(type);
        assertThat(validateConstraint(constraintId).getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint(constraintId)));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        TypeDescriptor typeDescriptor = (TypeDescriptor) row.get("Type");
        assertThat(typeDescriptor, typeDescriptor(type));
        store.commitTransaction();
    }

}
