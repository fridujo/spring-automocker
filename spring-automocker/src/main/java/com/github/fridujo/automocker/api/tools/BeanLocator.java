package com.github.fridujo.automocker.api.tools;

import com.google.common.base.Joiner;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.stream.Collectors;

public class BeanLocator {

    private final ApplicationContext applicationContext;

    public BeanLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @throws IllegalArgumentException when zero or more than one beans matches
     */
    public <T> T getBean(Class<T> beanClass) throws IllegalArgumentException {
        Map<String, T> matchingBeansByName = applicationContext.getBeansOfType(beanClass);
        return getOnlyOneOrThrow(beanClass.getSimpleName(), matchingBeansByName);
    }

    /**
     * @throws IllegalArgumentException when zero or more than one beans matches
     */
    public <T> T getBeanByPartialName(String partialName, Class<T> beanClass) {
        Map<String, T> matchingBeansByName = applicationContext.getBeansOfType(beanClass)
            .entrySet()
            .stream()
            .filter(beanAndName -> beanAndName.getKey().contains(partialName))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getOnlyOneOrThrow(beanClass.getSimpleName() + "{name: *" + partialName + "*}", matchingBeansByName);
    }

    private <T> T getOnlyOneOrThrow(String descriptor, Map<String, T> matchingBeansByName) throws IllegalArgumentException {
        if (matchingBeansByName.size() == 1) {
            return matchingBeansByName.values()
                .iterator()
                .next();
        } else if (matchingBeansByName.isEmpty()) {
            throw new IllegalArgumentException("No bean matching " + descriptor);
        } else {
            throw new IllegalArgumentException("Multiple beans matching " +
                descriptor +
                ". Available: " +
                Joiner.on(", ").join(matchingBeansByName.keySet())
            );
        }
    }
}
