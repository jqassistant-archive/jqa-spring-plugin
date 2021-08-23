package com.buschmais.jqassistant.plugin.spring.test.constraint;

import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.common.test.scanner.MapBuilder;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.spring.test.set.application.invalid.Controller;
import com.buschmais.jqassistant.plugin.spring.test.set.application.invalid.app.Application;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.PackageDescriptorMatcher.packageDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class AllTypesInApplicationPackageIT extends AbstractJavaPluginIT {

    @Test
    void allTypesInsideApplicationPackage() throws Exception {
        scanClassPathDirectory(getClassesDirectory(AllTypesInApplicationPackageIT.class));
        deletePackages(Application.class);
        assertThat(validateConstraint("spring-boot:AllTypesInApplicationPackage").getStatus(), equalTo(SUCCESS));
    }

    @Test
    void typeOutsideApplicationPackage() throws Exception {
        scanClassPathDirectory(getClassesDirectory(AllTypesInApplicationPackageIT.class));
        deletePackages(Controller.class);
        Result<Constraint> result = validateConstraint("spring-boot:AllTypesInApplicationPackage");
        assertThat(result.getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        assertThat(row.get("Application"), (Matcher<? super Object>) typeDescriptor(Application.class));
        assertThat(row.get("ApplicationPackage"), (Matcher<? super Object>) packageDescriptor(Application.class.getPackage()));
        TypeDescriptor typeOutsideApplicationPackage = (TypeDescriptor) row.get("TypeOutsideApplicationPackage");
        assertThat(typeOutsideApplicationPackage, typeDescriptor(Controller.class));
        store.commitTransaction();
    }

    private void deletePackages(Class<?> rootClass) {
        store.beginTransaction();
        Map<String, Object> params = MapBuilder.<String, Object> create("package", rootClass.getPackage().getName()).get();
        store.executeQuery("MATCH (p:Package) WHERE NOT p.fqn starts with {package} DETACH DELETE p", params);
        store.commitTransaction();
        // Clear the TX cache as the ids of the deleted nodes will be re-used by Neo4j when creating new nodes
        store.getXOManager().clear();
    }

}
