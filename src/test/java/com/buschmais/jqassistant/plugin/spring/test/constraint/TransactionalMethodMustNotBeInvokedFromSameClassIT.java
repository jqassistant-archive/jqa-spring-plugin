package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.transaction.TransactionalMethod;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionalMethodMustNotBeInvokedFromSameClassIT extends AbstractJavaPluginIT {

    @Test
    public void transactionMethodsMustNotBeCalledDirectly() throws Exception {
        scanClasses(TransactionalMethod.class);
        assertThat(validateConstraint("spring-transaction:TransactionalMethodMustNotBeInvokedFromSameClass").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result, result(constraint("spring-transaction:TransactionalMethodMustNotBeInvokedFromSameClass")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        store.commitTransaction();
    }

}
