package com.buschmais.jqassistant.plugin.spring.test.constraint;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.Result.Status;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.constructors.ServiceWritingConstructorField;

public class FieldsOfInjectablesMustNotBeManipulatedIT extends AbstractJavaPluginIT {

    @Test
    public void constructorFieldsMustNotBeManipulated() throws Exception {

    	    scanClasses(ServiceWritingConstructorField.class);

        Result<Constraint> result = validateConstraint("spring-injection:FieldsOfInjectablesMustNotBeManipulated");
        assertThat(result.getStatus(), equalTo(FAILURE));

        store.beginTransaction();
        
        String message = result.getRows().get(0).get("Result").toString();
        
        assertThat(message, containsString(ServiceWritingConstructorField.class.getName()));
        assertThat(message, containsString("value"));

        assertThat(result, result(constraint("spring-injection:FieldsOfInjectablesMustNotBeManipulated")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        MethodDescriptor method = (MethodDescriptor) rows.get(0).get("Method");
        assertThat(method, methodDescriptor(ServiceWritingConstructorField.class, "setValue", String.class));

        store.commitTransaction();
    }

}
