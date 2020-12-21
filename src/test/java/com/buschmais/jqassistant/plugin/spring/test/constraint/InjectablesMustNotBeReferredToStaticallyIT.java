package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Result.Status;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for rules rejecting static references to injectables.
 *
 * @author Oliver Gierke
 */
public class InjectablesMustNotBeReferredToStaticallyIT extends AbstractJavaPluginIT {

	@Test
	public void reportsInjectablesInStaticFields() throws Exception {

        scanClasses(MyComponent.class, MyDependency.class, MyDependencyImpl.class);

		Result<Constraint> result = validateConstraint("spring-injection:InjectablesMustNotBeHeldInStaticVariables");

		store.beginTransaction();

		assertThat(result.getStatus(), is(Status.FAILURE));
		assertThat(result.getRows(), hasSize(1));

		Map<String, Object> map = result.getRows().get(0);
		TypeDescriptor descriptor = (TypeDescriptor) map.get("Type");

		assertThat(descriptor, typeDescriptor(MyComponent.class));
        assertThat(map.get("Field"), is("dependency"));

		store.rollbackTransaction();
	}

	@Test
	public void reportsStaticReferenceToInjectable() throws Exception {

        scanClasses(MyComponent.class, MyDependency.class, MyDependencyImpl.class);

		Result<Constraint> result = validateConstraint("spring-injection:InjectablesMustNotBeAccessedStatically");

		store.beginTransaction();

		assertThat(result.getStatus(), is(Status.FAILURE));
		assertThat(result.getRows(), hasSize(1));

		Map<String, Object> map = result.getRows().get(0);
		MethodDescriptor descriptor = (MethodDescriptor) map.get("Method");

        assertThat(descriptor.getDeclaringType(), typeDescriptor(MyDependencyImpl.class));
		assertThat(descriptor.getName(), equalTo("someMethod"));

        assertThat(map.get("Field"), is("dependency"));

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
