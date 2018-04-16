package com.github.fridujo.automocker.api;

import java.lang.annotation.Annotation;

public interface AfterBeanRegistrationExecutable<A extends Annotation> {

    void execute(A annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry);
}
