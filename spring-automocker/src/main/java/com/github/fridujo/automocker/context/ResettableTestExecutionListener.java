package com.github.fridujo.automocker.context;

import com.github.fridujo.automocker.api.ResetMocks;
import com.github.fridujo.automocker.api.Resettable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class ResettableTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) {
        ResetMocks resetMocks = AnnotationUtils.getAnnotation(testContext.getTestClass(), ResetMocks.class);
        if (resetMocks != null && !resetMocks.disable()) {
            ApplicationContext applicationContext = testContext.getApplicationContext();
            for (Resettable resettable : applicationContext.getBeansOfType(Resettable.class).values()) {
                resettable.reset();
            }
        }
    }
}
