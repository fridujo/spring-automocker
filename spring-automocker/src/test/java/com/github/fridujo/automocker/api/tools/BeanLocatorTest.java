package com.github.fridujo.automocker.api.tools;

import com.github.fridujo.automocker.utils.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BeanLocatorTest {

    @Test
    void getBean_returns_when_one_bean_matches() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        BeanLocator beanLocator = new BeanLocator(applicationContext);

        when(applicationContext.getBeansOfType(String.class))
            .thenReturn(Maps.build(String.class, "testBeanName", "testBean"));

        assertThat(beanLocator.getBean(String.class)).isEqualTo("testBean");
    }

    @Test
    void getBean_throws_when_no_bean_matches() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        BeanLocator beanLocator = new BeanLocator(applicationContext);

        when(applicationContext.getBeansOfType(String.class))
            .thenReturn(Collections.emptyMap());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> beanLocator.getBean(String.class))
            .withMessage("No bean matching String");
    }

    @Test
    void getBean_throws_when_more_than_one_bean_matches() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        BeanLocator beanLocator = new BeanLocator(applicationContext);

        when(applicationContext.getBeansOfType(String.class))
            .thenReturn(Maps.build(String.class,
                "testBeanName1", "testBean1",
                "testBeanName2", "testBean2"
            ));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> beanLocator.getBean(String.class))
            .withMessage("Multiple beans matching String. Available: testBeanName1, testBeanName2");
    }

    @Test
    void getBeanByPartialName_returns_when_one_bean_matches() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        BeanLocator beanLocator = new BeanLocator(applicationContext);

        when(applicationContext.getBeansOfType(String.class))
            .thenReturn(Maps.build(String.class, "someTestBeanName", "testBean", "otherBeanName", "otherBean"));

        assertThat(beanLocator.getBeanByPartialName("Test", String.class)).isEqualTo("testBean");
    }

    @Test
    void getBeanByPartialName_throws_when_no_bean_matches() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        BeanLocator beanLocator = new BeanLocator(applicationContext);

        when(applicationContext.getBeansOfType(String.class))
            .thenReturn(Collections.emptyMap());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> beanLocator.getBeanByPartialName("Test", String.class))
            .withMessage("No bean matching String{name: *Test*}");
    }

    @Test
    void getBeanByPartialName_throws_when_more_than_one_bean_matches() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        BeanLocator beanLocator = new BeanLocator(applicationContext);

        when(applicationContext.getBeansOfType(String.class))
            .thenReturn(Maps.build(String.class,
                "testBeanName1", "testBean1",
                "testBeanName2", "testBean2"
            ));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> beanLocator.getBeanByPartialName("Bean", String.class))
            .withMessage("Multiple beans matching String{name: *Bean*}. Available: testBeanName1, testBeanName2");
    }
}
