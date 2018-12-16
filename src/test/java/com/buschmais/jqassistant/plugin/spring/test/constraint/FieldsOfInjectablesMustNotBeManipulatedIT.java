package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.constructors.ServiceWritingConstructorField;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FieldsOfInjectablesMustNotBeManipulatedIT extends AbstractJavaPluginIT {

    @Test
    public void constructorFieldsMustNotBeManipulated() throws Exception {

        scanClasses(ServiceWritingConstructorField.class);

        Result<Constraint> result = validateConstraint("spring-injection:FieldsOfInjectablesMustNotBeManipulated");
        assertThat(result.getStatus(), equalTo(FAILURE));

        store.beginTransaction();

        List<Map<String, Object>> rows = result.getRows();
        String message = rows.get(0).get("Message").toString();

        assertThat(message, containsString(ServiceWritingConstructorField.class.getName()));
        assertThat(message, containsString("'value'"));

        assertThat(rows.size(), equalTo(1));
        MethodDescriptor method = (MethodDescriptor) rows.get(0).get("Method");
        assertThat(method, methodDescriptor(ServiceWritingConstructorField.class, "setValue", String.class));

        store.commitTransaction();

        store.beginTransaction();
        for (FieldDescriptor field : query("MATCH (:Injectable)-[:DECLARES]->(field:Field) RETURN field").<FieldDescriptor>getColumn("field")) {
            field.setSynthetic(true);
        }
        store.commitTransaction();

        assertThat(validateConstraint("spring-injection:FieldsOfInjectablesMustNotBeManipulated").getStatus(), equalTo(SUCCESS));
    }
}
