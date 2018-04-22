package com.github.fridujo.automocker.utils;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

class PropertiesBuilderTest {

    @Test
    void trivial_singleton_properties() {
        Properties properties = PropertiesBuilder.of("testKey", "testValue");

        assertThat(properties).containsOnly(entry("testKey", "testValue"));
    }
}
