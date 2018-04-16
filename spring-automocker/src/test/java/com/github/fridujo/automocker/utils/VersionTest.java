package com.github.fridujo.automocker.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.fridujo.automocker.utils.Version.major;
import static com.github.fridujo.automocker.utils.Version.spring;
import static org.assertj.core.api.Assertions.assertThat;

class VersionTest {

    static Stream<Arguments> before_behaves_as_expected() {
        return Stream.of(
            Arguments.of(major(0).minor(0), major(0).minor(1), true),
            Arguments.of(major(0).minor(0), major(1).minor(0), true),
            Arguments.of(major(1).minor(0), major(0).minor(1), false),
            Arguments.of(major(1).minor(4), major(1).minor(4), false)
        );
    }

    @ParameterizedTest(name = "{0} is before {1} = {2}")
    @MethodSource
    void before_behaves_as_expected(Version first, Version second, boolean before) {
        assertThat(first.isBefore(second)).isEqualTo(before);
    }

    @Test
    void spring_version_is_after_4_3() {
        assertThat(major(4).minor(3).isBefore(spring()));
    }
}
