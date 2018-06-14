package com.github.fridujo.automocker.api;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ExtendedBeanDefinitionRegistryTest {

    @Test
    void registerBeanDefinition_by_class_registers_it_with_custom_name() {
        ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry = mock(ExtendedBeanDefinitionRegistry.class);
        doCallRealMethod().when(extendedBeanDefinitionRegistry).registerBeanDefinition(any());

        extendedBeanDefinitionRegistry.registerBeanDefinition(String.class);

        ArgumentCaptor<AbstractBeanDefinition> beanDefinitionArgumentCaptor = ArgumentCaptor.forClass(AbstractBeanDefinition.class);
        verify(extendedBeanDefinitionRegistry, times(1)).registerBeanDefinition(eq("AutomockerString"), beanDefinitionArgumentCaptor.capture());

        AbstractBeanDefinition registeredBeanDefinition = beanDefinitionArgumentCaptor.getValue();
        assertThat(registeredBeanDefinition).isInstanceOf(RootBeanDefinition.class);
        assertThat(registeredBeanDefinition.getBeanClass()).isEqualTo(String.class);
    }

    @Test
    void registerBeanDefinition_with_qualifiers_adds_them_before_registering() {
        ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry = mock(ExtendedBeanDefinitionRegistry.class);
        doCallRealMethod().when(extendedBeanDefinitionRegistry).registerBeanDefinition(any(), any(), any());

        Map<String, Object> qualifiers = new HashMap<>();
        qualifiers.put(Qualifier.class.getName(), "test1");
        AbstractBeanDefinition beanDefinition = mock(AbstractBeanDefinition.class);
        extendedBeanDefinitionRegistry.registerBeanDefinition("testBeanName", beanDefinition, qualifiers);

        ArgumentCaptor<AbstractBeanDefinition> beanDefinitionArgumentCaptor = ArgumentCaptor.forClass(AbstractBeanDefinition.class);
        verify(extendedBeanDefinitionRegistry, times(1)).registerBeanDefinition(eq("testBeanName"), eq(beanDefinition));

        ArgumentCaptor<AutowireCandidateQualifier> autowireCandidateQualifierArgumentCaptor = ArgumentCaptor.forClass(AutowireCandidateQualifier.class);
        verify(beanDefinition, times(1)).addQualifier(autowireCandidateQualifierArgumentCaptor.capture());
        AutowireCandidateQualifier autowireCandidateQualifier = autowireCandidateQualifierArgumentCaptor.getValue();
        assertThat(autowireCandidateQualifier.getTypeName()).isEqualTo(Qualifier.class.getName());
        assertThat(autowireCandidateQualifier.getAttribute("value")).isEqualTo("test1");
    }
}
