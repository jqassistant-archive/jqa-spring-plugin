package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.common.api.model.DependsOnDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.JavaClassesDirectoryDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ConfigurationBean;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ConfigurationWithBeanProducer;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.ControllerInstantiatingService;
import com.buschmais.jqassistant.plugin.spring.test.set.injectables.Service;

import org.hamcrest.Matcher;
import org.junit.Test;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.analysis.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class InjectablesMustNotBeInstatiatedIT extends AbstractJavaPluginIT {

    @Test
    public void applicationClasses() throws Exception {
        scanClasses("a", ControllerInstantiatingService.class, Service.class, ConfigurationWithBeanProducer.class, ConfigurationBean.class);
        Result<Constraint> result = validateConstraint("spring-injection:InjectablesMustNotBeInstantiated");
        assertThat(result.getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        assertThat(result, result(constraint("spring-injection:InjectablesMustNotBeInstantiated")));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        assertThat(row.get("Type"), (Matcher<? super Object>) typeDescriptor(ControllerInstantiatingService.class));
        assertThat(row.get("Method"), (Matcher<? super Object>) methodDescriptor(ControllerInstantiatingService.class, "instantiateService"));
        assertThat(row.get("Injectable"), (Matcher<? super Object>) typeDescriptor(Service.class));
        store.commitTransaction();
    }

    @Test
    public void testClasses() throws Exception {
        store.beginTransaction();
        JavaClassesDirectoryDescriptor jar = getArtifactDescriptor("a");
        jar.setType("jar");
        JavaClassesDirectoryDescriptor testJar = getArtifactDescriptor("b");
        testJar.setType("test-jar");
        store.create(testJar, DependsOnDescriptor.class, jar);
        store.commitTransaction();
        scanClasses("a", Service.class);
        scanClasses("b", ControllerInstantiatingService.class);
        assertThat(validateConstraint("spring-injection:InjectablesMustNotBeInstantiated").getStatus(), equalTo(SUCCESS));
    }
}
