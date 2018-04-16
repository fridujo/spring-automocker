package com.github.fridujo.automocker.api;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(AfterBeanRegistration.AfterBeanRegistrations.class)
public @interface AfterBeanRegistration {

    Class<? extends AfterBeanRegistrationExecutable> value();

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface AfterBeanRegistrations {
        AfterBeanRegistration[] value();
    }
}
