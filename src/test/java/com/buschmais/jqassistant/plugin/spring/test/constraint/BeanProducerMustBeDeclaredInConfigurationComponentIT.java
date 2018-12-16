package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ConfigurationBean;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ConfigurationWithBeanProducer;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ServiceWithBeanProducer;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BeanProducerMustBeDeclaredInConfigurationComponentIT extends AbstractJavaPluginIT {

    @Test
    public void beanProducerInConfigurationComponent() throws Exception {
        scanClasses(ConfigurationWithBeanProducer.class);
        assertThat(validateConstraint("spring-injection:BeanProducerMustBeDeclaredInConfigurationComponent").getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void beanProducerInServiceComponent() throws Exception {
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
}
