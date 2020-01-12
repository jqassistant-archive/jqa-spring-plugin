package com.buschmais.jqassistant.plugin.spring.test.set.constructors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security")
@Getter
@Setter
public class SecurityProperties {
    private boolean enabled = true;
}
