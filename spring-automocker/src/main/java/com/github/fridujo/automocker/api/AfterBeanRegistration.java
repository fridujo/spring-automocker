package com.github.fridujo.automocker.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
