package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.ResetMocks;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@MockKafka
public @interface Automocker {
}


