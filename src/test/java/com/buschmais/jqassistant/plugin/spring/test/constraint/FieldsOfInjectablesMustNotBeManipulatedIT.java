package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.WritesDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.constructors.Repository;
import com.buschmais.jqassistant.plugin.spring.test.set.constructors.RepositoryImpl;
import com.buschmais.jqassistant.plugin.spring.test.set.constructors.SecurityProperties;
import com.buschmais.jqassistant.plugin.spring.test.set.constructors.ServiceImpl;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.FieldDescriptorMatcher.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class FieldsOfInjectablesMustNotBeManipulatedIT extends AbstractJavaPluginIT {

    @Test
    public void constructorFieldsMustNotBeManipulated() throws Exception {
        // given
        scanClasses(ServiceImpl.class, Repository.class, RepositoryImpl.class);
        // when
        Result<Constraint> result = validateConstraint("spring-injection:FieldsOfInjectablesMustNotBeManipulated");
        // then
        assertThat(result.getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        WritesDescriptor writeToInjectableField = (WritesDescriptor) row.get("WriteToInjectableField");
        TypeDescriptor injectable = (TypeDescriptor) row.get("Injectable");
        FieldDescriptor field = (FieldDescriptor) row.get("Field");
        assertThat(writeToInjectableField.getLineNumber(), equalTo(55));
        assertThat(injectable, typeDescriptor(ServiceImpl.class));
        assertThat(field, fieldDescriptor(ServiceImpl.class, "repository"));

        store.commitTransaction();
    }

    @Test
    public void configurationProperties() throws Exception {
        // given
        scanClasses(SecurityProperties.class);
        // when
        Result<Constraint> result = validateConstraint("spring-injection:FieldsOfInjectablesMustNotBeManipulated");
        // then
        assertThat(result.getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void syntheticFields() throws Exception {
        // given
        scanClasses(ServiceImpl.class, RepositoryImpl.class);
        store.beginTransaction();
        Map<String, Object> params = new HashMap<>();
        params.put("service", ServiceImpl.class.getName());
        List<FieldDescriptor> fields = query("MATCH (:Type{fqn:$service})-[:DECLARES]->(field:Field) SET field.synthetic=true RETURN field", params)
                .getColumn("field");
        assertThat(fields, hasItems(fieldDescriptor(ServiceImpl.class, "repository")));
        store.commitTransaction();
        // when
        Result.Status status = validateConstraint("spring-injection:FieldsOfInjectablesMustNotBeManipulated").getStatus();
        // then
        assertThat(status, equalTo(SUCCESS));
    }
}
