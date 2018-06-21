package com.github.fridujo.automocker.context;

import com.github.fridujo.automocker.api.Resettable;
import com.github.fridujo.automocker.base.Automocker;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResettableTestExecutionListenerTest {

    @Test
    void all_beans_implementing_resettable_are_reset() throws Exception {
        TestExecutionListener resettableTestExecutionListener = new ResettableTestExecutionListener();

        Set<Resettable> resettableBeans = Stream.generate(ResettableBean::new).limit(3).collect(Collectors.toSet());

        TestContext testContext = mock(TestContext.class, RETURNS_DEEP_STUBS);
        when(testContext.getTestClass()).thenReturn((Class) TestClass.class);
        when(testContext.getApplicationContext().getBeansOfType(eq(Resettable.class)).values())
            .thenReturn(resettableBeans);
        resettableTestExecutionListener.afterTestMethod(testContext);

        assertThat(resettableBeans)
            .extracting("resetCount")
            .as("Reset field collection")
            .containsExactlyInAnyOrder(1, 1, 1);

        resettableTestExecutionListener.afterTestClass(testContext);

        assertThat(resettableBeans)
            .extracting("resetCount")
            .as("Reset field collection")
            .containsExactlyInAnyOrder(2, 2, 2);

        resettableTestExecutionListener.afterTestExecution(testContext);

        assertThat(resettableBeans)
            .extracting("resetCount")
            .as("Reset field collection")
            .containsExactlyInAnyOrder(3, 3, 3);
    }

    @Automocker
    static class TestClass {
    }

    static class ResettableBean implements Resettable {

        private int resetCount = 0;

        @Override
        public void reset() {
            resetCount ++;
        }
    }
}
