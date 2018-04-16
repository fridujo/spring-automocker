package com.github.fridujo.automocker.base;

import java.lang.annotation.*;

/**
 * Collection of mocking strategies supplied by default.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@MockPropertySources
@MockWebMvc
public @interface Automocker {
}
