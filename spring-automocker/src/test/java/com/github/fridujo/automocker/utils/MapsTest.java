package com.github.fridujo.automocker.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.data.MapEntry.entry;

class MapsTest {

    @Test
    void simple_case() {
        Map<Integer, String> labelsByIndex = Maps.build(Integer.class, 1, "test1", 2, "test2");
        assertThat(labelsByIndex).containsOnly(entry(1, "test1"), entry(2, "test2"));
    }

    @Test
    void odd_number_of_key_and_values() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Maps.build(String.class, "key1"))
            .withMessage("keysAndValues array must be even");
    }
}
