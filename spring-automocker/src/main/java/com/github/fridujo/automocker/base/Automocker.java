package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.ResetMocks;

import java.lang.annotation.*;

/**
 * Collection of mocking strategies supplied by default.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@RegisterTools
@ResetMocks
@MockPropertySources
@MockWebMvc
@MockJdbc
@MockJms
@MockMicrometerGraphite
@MockAmqp
public @interface Automocker {
}


