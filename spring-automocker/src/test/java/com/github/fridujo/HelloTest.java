package com.github.fridujo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloTest {

    @Test
    void hello() {
        Hello hello = new Hello();
        Assertions.assertThat(hello.world()).contains("world");
    }
}
