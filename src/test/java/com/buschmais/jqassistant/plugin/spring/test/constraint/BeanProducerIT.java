package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ConfigurationBean;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ConfigurationWithBeanProducer;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ServiceInvokingBeanProducer;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ServiceWithBeanProducer;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

class BeanProducerIT extends AbstractJavaPluginIT {

    @Test
    public void beanProducerInConfigurationComponent() throws Exception {
        scanClasses(ConfigurationWithBeanProducer.class);
        assertThat(validateConstraint("spring-injection:BeanProducerMustBeDeclaredInConfigurationComponent").getStatus(), equalTo(SUCCESS));
    }

    @Test
    void beanProducerInServiceComponent() throws Exception {
        scanClasses(ServiceWithBeanProducer.class);
        Result<Constraint> result = validateConstraint("spring-injection:BeanProducerMustBeDeclaredInConfigurationComponent");
        store.beginTransaction();
        assertThat(result.getStatus(), equalTo(FAILURE));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        assertThat((MethodDescriptor) row.get("BeanProducer"), methodDescriptor(ServiceWithBeanProducer.class, "getBean"));
        assertThat((TypeDescriptor) row.get("Injectable"), typeDescriptor(ConfigurationBean.class));
        store.commitTransaction();
    }

    @Test
    void beanProducerMustNotBeInvokedDirectly() throws Exception {
        scanClasses(ConfigurationWithBeanProducer.class, ServiceInvokingBeanProducer.class);
        Result<Constraint> result = validateConstraint("spring-injection:BeanProducerMustNotBeInvokedDirectly");
        store.beginTransaction();
        assertThat(result.getStatus(), equalTo(FAILURE));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        assertThat((TypeDescriptor) row.get("Type"), typeDescriptor(ServiceInvokingBeanProducer.class));
        assertThat((MethodDescriptor) row.get("Method"), methodDescriptor(ServiceInvokingBeanProducer.class, "doSomething"));
        assertThat((TypeDescriptor) row.get("BeanProducerType"), typeDescriptor(ConfigurationWithBeanProducer.class));
        assertThat((MethodDescriptor) row.get("BeanProducer"), methodDescriptor(ConfigurationWithBeanProducer.class, "getConfiguration"));
        assertThat((Integer) row.get("LineNumber"), greaterThan(0));
        store.commitTransaction();
    }
}
