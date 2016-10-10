package com.buschmais.jqassistant.plugin.spring.test.constraint;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.PackageDescriptorMatcher.packageDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.plugin.common.test.scanner.MapBuilder;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.application.invalid.TestController;
import com.buschmais.jqassistant.plugin.spring.test.set.application.invalid.app.Application;

public class AllTypesInApplicationPackageIT extends AbstractJavaPluginIT {

    @Test
    public void allTypesInsideApplicationPackage() throws Exception {
        scanClassPathDirectory(getClassesDirectory(AllTypesInApplicationPackageIT.class));
        store.beginTransaction();
        deletePackages(Application.class);
        store.commitTransaction();
        assertThat(validateConstraint("spring-boot:AllTypesInApplicationPackage").getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void typeOutsideApplicationPackage() throws Exception {
        scanClassPathDirectory(getClassesDirectory(AllTypesInApplicationPackageIT.class));
        store.beginTransaction();
        deletePackages(TestController.class);
        store.commitTransaction();
        Result<Constraint> result = validateConstraint("spring-boot:AllTypesInApplicationPackage");
        assertThat(result.getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        assertThat(row.get("Application"), (Matcher<? super Object>) typeDescriptor(Application.class));
        assertThat(row.get("ApplicationPackage"), (Matcher<? super Object>) packageDescriptor(Application.class.getPackage()));
        List<TypeDescriptor> typesOutsideApplicationPackage = (List<TypeDescriptor>) row.get("TypesOutsideApplicationPackage");
        assertThat(typesOutsideApplicationPackage.size(), equalTo(1));
        assertThat(typesOutsideApplicationPackage.get(0), typeDescriptor(TestController.class));
        store.commitTransaction();
    }

    private void deletePackages(Class<?> rootClass) {
        Map<String, Object> params = MapBuilder.<String, Object> create("package", rootClass.getPackage().getName()).get();
        store.executeQuery("MATCH (p:Package) WHERE NOT p.fqn starts with {package} DETACH DELETE p", params);
    }

}
