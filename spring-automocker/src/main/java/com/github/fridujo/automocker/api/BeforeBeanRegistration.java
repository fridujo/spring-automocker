package com.github.fridujo.automocker.api;

import java.lang.annotation.*;

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
