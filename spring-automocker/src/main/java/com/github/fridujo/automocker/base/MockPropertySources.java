package com.github.fridujo.automocker.base;

import com.github.fridujo.automocker.api.BeforeBeanRegistration;
import com.github.fridujo.automocker.api.BeforeBeanRegistrationExecutable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.lang.annotation.*;

/**
 * Make Spring ignore {@link org.springframework.context.annotation.PropertySource} annotations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@BeforeBeanRegistration(MockPropertySources.MockPropertySourcesExecutable.class)
public @interface MockPropertySources {

    class MockPropertySourcesExecutable implements BeforeBeanRegistrationExecutable<MockPropertySources> {
        static ProtocolResolver MOCK_PROPERTIES_PROTOCOL_RESOLVER = new MockPropertiesProtocolResolver();

        @Override
        public void execute(MockPropertySources annotation, ConfigurableApplicationContext context) {
            context.addProtocolResolver(MOCK_PROPERTIES_PROTOCOL_RESOLVER);
        }

        private static class MockPropertiesProtocolResolver implements ProtocolResolver {
            @Override
            public Resource resolve(String location, ResourceLoader resourceLoader) {
                // TODO improve to exclude only files that are detected through class scanning
                if (location.endsWith(".properties")
                    && !location.contains("META-INF")
                    && !location.contains("git.properties")) {

                    return new ByteArrayResource(new byte[0]);
                } else {
                    return null;
                }
            }
        }
    }
}
