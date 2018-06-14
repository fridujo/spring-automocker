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
@Repeatable(BeforeBeanRegistration.BeforeBeanRegistrations.class)
public @interface BeforeBeanRegistration {

    Class<? extends BeforeBeanRegistrationExecutable> value();

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface BeforeBeanRegistrations {
        BeforeBeanRegistration[] value();
    }
}
