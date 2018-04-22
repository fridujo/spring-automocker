package com.github.fridujo.automocker.api;

import java.lang.annotation.*;

/**
 * Activate a {@link org.springframework.test.context.TestExecutionListener} to
 * reset any bean implementing {@link Resettable}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResetMocks {
    boolean disable() default false;
}
