package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.fieldinjection.Repository;
import com.buschmais.jqassistant.plugin.spring.test.set.fieldinjection.ServiceWithConstructorInjection;
import com.buschmais.jqassistant.plugin.spring.test.set.fieldinjection.ServiceWithFieldInjection;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.FieldDescriptorMatcher.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class FieldInjectionIT extends AbstractJavaPluginIT {

    @Test
    void serviceWithFieldInjection() throws Exception {
        scanClasses(ServiceWithFieldInjection.class);
        verifyConstraintResult(ServiceWithFieldInjection.class, "repository");
    }

    @Test
    void serviceWithConstructorInjection() throws Exception {
        scanClasses(ServiceWithConstructorInjection.class);
        assertThat(validateConstraint("spring-injection:FieldInjectionIsNotAllowed").getStatus(), equalTo(SUCCESS));
    }

    @Test
    void rejectsNonFinalInjectableField() throws Exception {
        scanClasses(ServiceWithConstructorInjection.class, Repository.class);

        Result<Constraint> constraint = validateConstraint("spring-injection:InjectablesShouldBeHeldInFinalFields");

        assertThat(constraint.getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        Map<String, Object> row = constraint.getRows().get(0);
        TypeDescriptor injectableType = (TypeDescriptor) row.get("InjectableType");
        assertThat(injectableType, typeDescriptor(ServiceWithConstructorInjection.class));
        FieldDescriptor injectableField = (FieldDescriptor) row.get("InjectableField");
        assertThat(injectableField, fieldDescriptor(ServiceWithConstructorInjection.class,"repository"));
        store.commitTransaction();

        store.beginTransaction();
        for (FieldDescriptor field : query("MATCH (:Injectable)-[:DECLARES]->(field:Field) RETURN field").<FieldDescriptor>getColumn("field")) {
            field.setSynthetic(true);
        }
        store.commitTransaction();

        assertThat(validateConstraint("spring-injection:InjectablesShouldBeHeldInFinalFields").getStatus(), equalTo(SUCCESS));
    }

    @Test
    void doesNotRejectFinalInjectableField() throws Exception {
        scanClasses(SomeComponent.class);

        assertThat(validateConstraint("spring-injection:InjectablesShouldBeHeldInFinalFields").getStatus(), is(SUCCESS));
    }

    private void verifyConstraintResult(Class<?> type, String fieldName) throws Exception {
        assertThat(validateConstraint("spring-injection:FieldInjectionIsNotAllowed").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint("spring-injection:FieldInjectionIsNotAllowed")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        TypeDescriptor typeDescriptor = (TypeDescriptor) row.get("Type");
        FieldDescriptor fieldDescriptor = (FieldDescriptor) row.get("Field");
        assertThat(typeDescriptor, typeDescriptor(type));
        assertThat(fieldDescriptor, fieldDescriptor(type, fieldName));
        store.commitTransaction();
    }

    @Component
    static class SomeComponent {

        private final SomeComponent dependency;

        public SomeComponent(SomeComponent dependency) {
            this.dependency = dependency;
        }
    }
}
