package com.buschmais.jqassistant.plugin.spring.test.constraint;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Result.Status;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the constraint 'spring-injection:InjectablesMustOnlyBeHeldInInjectables'
 *
 * @author Stephan Pirnbaum
 */
public class InjectablesMustOnlyBeHeldInInjectablesIT extends AbstractJavaPluginIT {

    @Test
    public void reportsInjectablesInNonInjectableClasses() throws Exception {
        scanClasses(
            Injectable.class,
            InvalidComponent.class, ValidComponent.class,
            InvalidAbstractComponent.class, InvalidAbstractComponentImpl.class,
            ValidAbstractComponent.class, ValidAbstractComponentImpl.class);

        Result<Constraint> result = validateConstraint("spring-injection:InjectablesMustOnlyBeHeldInInjectables");

        store.beginTransaction();

        assertThat(result.getStatus()).isEqualTo(Status.FAILURE);
        assertThat(result.getRows()).hasSize(3);

        assertThat(result.getRows().get(0).get("NonInjectableHavingInjectablesAsField"))
            .isEqualTo("com.buschmais.jqassistant.plugin.spring.test.constraint.InjectablesMustOnlyBeHeldInInjectablesIT$InvalidAbstractComponent");
        assertThat(result.getRows().get(1).get("NonInjectableHavingInjectablesAsField"))
            .isEqualTo("com.buschmais.jqassistant.plugin.spring.test.constraint.InjectablesMustOnlyBeHeldInInjectablesIT$InvalidAbstractComponentImpl");
        assertThat(result.getRows().get(2).get("NonInjectableHavingInjectablesAsField"))
            .isEqualTo("com.buschmais.jqassistant.plugin.spring.test.constraint.InjectablesMustOnlyBeHeldInInjectablesIT$InvalidComponent");

        store.rollbackTransaction();
    }

    @RequiredArgsConstructor
    static class InvalidComponent {
        private final Injectable injectable;
    }

    @RequiredArgsConstructor
    @Component
    static class ValidComponent {
        private final Injectable injectable;
    }

    @RequiredArgsConstructor
    abstract static class InvalidAbstractComponent {
        private final Injectable injectable;
    }

    static class InvalidAbstractComponentImpl extends InvalidAbstractComponent {

        private final Injectable injectable;

        public InvalidAbstractComponentImpl(Injectable injectable) {
            super(injectable);
            this.injectable = injectable;
        }
    }

    @RequiredArgsConstructor
    abstract static class ValidAbstractComponent {
        private final Injectable injectable;
    }

    @Component
    static class ValidAbstractComponentImpl extends ValidAbstractComponent {
        public ValidAbstractComponentImpl(Injectable injectable) {
            super(injectable);
        }
    }

    @Component
    static class Injectable {
    }

}
