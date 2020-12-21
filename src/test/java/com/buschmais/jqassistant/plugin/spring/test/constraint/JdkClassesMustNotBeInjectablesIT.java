package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Result.Status;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for rules rejecting static references to injectables.
 *
 * @author Stephan Pirnbaum
 */
public class JdkClassesMustNotBeInjectablesIT extends AbstractJavaPluginIT {

	@Test
	public void reportsInjectableJdkClasses() throws Exception {

        scanClasses(MyComponent.class);

		Result<Constraint> result = validateConstraint("spring-injection:JdkClassesMustNotBeInjectables");

		store.beginTransaction();

		assertThat(result.getStatus()).isEqualTo(Status.FAILURE);
		assertThat(result.getRows()).hasSize(1);

		Map<String, Object> map = result.getRows().get(0);

		assertThat(map.get("Injectable")).isEqualTo(Object.class.getName());

		store.rollbackTransaction();
	}

	@Component
	static class MyComponent {

	    @Bean
        public Object someMethod() {
	        return null;
        }

	}

}
