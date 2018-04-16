package com.github.fridujo.automocker.api;

import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;

public interface BeforeBeanRegistrationExecutable<A extends Annotation> {

    void execute(A annotation, ConfigurableApplicationContext context);
}
