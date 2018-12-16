package com.buschmais.jqassistant.plugin.spring.test.concept;

import java.util.List;

import com.buschmais.jqassistant.plugin.spring.test.set.transaction.TransactionalClass;
import com.buschmais.jqassistant.plugin.spring.test.set.transaction.TransactionalMethod;
import com.buschmais.jqassistant.plugin.spring.test.set.transaction.TransactionalSubClass;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


public class TransactionalMethodIT extends AbstractSpringIT {

    @Test
    public void transactionalMethod() throws Exception {
        scanClasses(TransactionalMethod.class);
        assertThat(applyConcept("spring-transaction:TransactionalMethod").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> methods = query("MATCH (m:Spring:Method:Transactional) RETURN m").getColumn("m");
        assertThat(methods.size(), equalTo(1));
        assertThat(methods, hasItem(methodDescriptor(TransactionalMethod.class, "transactionalMethod")));
        assertThat(methods, not(hasItem(methodDescriptor(TransactionalMethod.class, "nonTransactionalMethod"))));
        store.commitTransaction();
    }

    @Test
    public void transactionalClass() throws Exception {
        scanClasses(TransactionalClass.class, TransactionalSubClass.class);
        assertThat(applyConcept("spring-transaction:TransactionalClass").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> classes = query("MATCH (c:Spring:Class:Transactional) RETURN c").getColumn("c");
        assertThat(classes.size(), equalTo(2));
        assertThat(classes, hasItem(typeDescriptor(TransactionalClass.class)));
        assertThat(classes, hasItem(typeDescriptor(TransactionalSubClass.class)));
        List<Object> methods = query("MATCH (m:Spring:Method:Transactional) RETURN m").getColumn("m");
        assertThat(methods.size(), equalTo(2));
        assertThat(methods, hasItem(methodDescriptor(TransactionalClass.class, "transactionalMethod")));
        assertThat(methods, hasItem(methodDescriptor(TransactionalSubClass.class, "transactionalSubClassMethod")));
        store.commitTransaction();
    }
}
