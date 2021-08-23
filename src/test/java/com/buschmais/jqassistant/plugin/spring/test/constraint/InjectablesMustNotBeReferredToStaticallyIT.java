package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Result.Status;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static com.buschmais.jqassistant.plugin.java.test.matcher.FieldDescriptorMatcher.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for rules rejecting static references to injectables.
 *
 * @author Oliver Gierke
 */
class InjectablesMustNotBeReferredToStaticallyIT extends AbstractJavaPluginIT {

    @Test
    void reportsInjectablesInStaticFields() throws Exception {

        scanClasses(MyComponent.class, MyDependency.class, MyDependencyImpl.class);

        Result<Constraint> result = validateConstraint("spring-injection:InjectablesMustNotBeHeldInStaticVariables");

        store.beginTransaction();

        assertThat(result.getStatus(), is(Status.FAILURE));
        assertThat(result.getRows(), hasSize(1));

        Map<String, Object> map = result.getRows().get(0);

        assertThat((TypeDescriptor) map.get("Type"), typeDescriptor(MyComponent.class));
        assertThat((FieldDescriptor)map.get("Field"), fieldDescriptor(MyComponent.class, "dependency"));

        store.rollbackTransaction();
    }

    @Test
    void reportsStaticReferenceToInjectable() throws Exception {

        scanClasses(MyComponent.class, MyDependency.class, MyDependencyImpl.class);

        Result<Constraint> result = validateConstraint("spring-injection:InjectablesMustNotBeAccessedStatically");

        store.beginTransaction();

        assertThat(result.getStatus(), is(Status.FAILURE));
        assertThat(result.getRows(), hasSize(1));

        Map<String, Object> map = result.getRows().get(0);
        assertThat((TypeDescriptor) map.get("Type"), typeDescriptor(MyDependencyImpl.class));
        assertThat((MethodDescriptor) map.get("Method"), methodDescriptor(MyDependencyImpl.class, "someMethod"));
        assertThat((FieldDescriptor) map.get("Field"), fieldDescriptor(MyComponent.class, "dependency"));

        store.rollbackTransaction();
    }

    @Component
    static class MyComponent {
        static MyDependency dependency;
    }

    interface MyDependency {
    }

    @Component
    static class MyDependencyImpl implements MyDependency {

        public static MyDependency someMethod() {
            return MyComponent.dependency;
        }
    }
}
