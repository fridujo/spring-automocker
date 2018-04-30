package com.github.fridujo.sample.propertySource;


import com.github.fridujo.automocker.base.Automocker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Automocker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PropertySourceApplication.class)
@TestPropertySource(properties = {
    "text.literal = literal Test Text",
    "text.optional=optionalText"})
class PropertySourceApplicationTest {

    @Autowired
    private TextService service;

    @Value("${missing.key:}")
    private Optional<String> emptyOptional;

    @Test
    void property_source_is_mocked() {
        assertThat(service.getLiteralText()).isEqualTo("literal Test Text");
        assertThat(service.getOptionalText()).isEqualTo("optionalText");
        assertThat(emptyOptional).isEmpty();
    }
}
