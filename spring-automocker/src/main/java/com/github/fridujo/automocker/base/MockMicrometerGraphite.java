package com.github.fridujo.automocker.base;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.github.fridujo.automocker.api.AfterBeanRegistration;
import com.github.fridujo.automocker.api.AfterBeanRegistrationExecutable;
import com.github.fridujo.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.fridujo.automocker.api.metrics.GraphiteMock;
import com.github.fridujo.automocker.api.metrics.GraphiteSenderMock;
import com.github.fridujo.automocker.utils.Classes;
import io.micrometer.core.instrument.Clock;
import io.micrometer.graphite.GraphiteConfig;
import io.micrometer.graphite.GraphiteHierarchicalNameMapper;
import io.micrometer.graphite.GraphiteMeterRegistry;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockMicrometerGraphite.MockGraphiteExecutable.class)
public @interface MockMicrometerGraphite {

    class MockGraphiteExecutable implements AfterBeanRegistrationExecutable<MockMicrometerGraphite> {

        @Override
        public void execute(MockMicrometerGraphite annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            if (Classes.isPresent("io.micrometer.graphite.GraphiteMeterRegistry")) {
                modifyGraphiteMeterRegistryBeanDefinitions(extendedBeanDefinitionRegistry);
            }
        }

        private void modifyGraphiteMeterRegistryBeanDefinitions(ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> graphiteSenderBeans = extendedBeanDefinitionRegistry
                .getBeanDefinitionsForClass(GraphiteMeterRegistry.class);
            if (!graphiteSenderBeans.isEmpty()) {
                String graphiteConfigBeanName = extendedBeanDefinitionRegistry.getBeanDefinitionsForClass(GraphiteConfig.class).iterator().next().name();

                String graphiteReporterFactoryBeanName = extendedBeanDefinitionRegistry.registerBeanDefinition(GraphiteReporterFactory.class);
                String graphiteMeterRegistryFactoryBeanName = extendedBeanDefinitionRegistry.registerBeanDefinition(GraphiteMeterRegistryFactory.class);
                graphiteSenderBeans.forEach(meta -> {
                    String graphiteSenderMockBeanName = meta.registerLinkedBeanDefinition(GraphiteSenderMock.class);
                    String metricRegistryBeanName = meta.registerLinkedBeanDefinition(MetricRegistry.class);

                    String graphiteReporterBeanName = "Automocker" + meta.name() + "GraphiteReporter";
                    RootBeanDefinition graphiteReporterBeanDefinition = new RootBeanDefinition(GraphiteReporter.class);
                    graphiteReporterBeanDefinition.setFactoryBeanName(graphiteReporterFactoryBeanName);
                    graphiteReporterBeanDefinition.setFactoryMethodName("createGraphiteReporter");
                    graphiteReporterBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(metricRegistryBeanName));
                    graphiteReporterBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, new RuntimeBeanReference(graphiteConfigBeanName));
                    graphiteReporterBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(2, new RuntimeBeanReference(graphiteSenderMockBeanName));
                    extendedBeanDefinitionRegistry.registerBeanDefinition(graphiteReporterBeanName, graphiteReporterBeanDefinition, meta.getBeanQualifiers());

                    String graphiteMockBeanName = "Automocker" + meta.name() + "GraphiteMock";
                    RootBeanDefinition graphiteMockBeanDefinition = new RootBeanDefinition(GraphiteMock.class);
                    graphiteMockBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(graphiteSenderMockBeanName));
                    graphiteMockBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, new RuntimeBeanReference(graphiteReporterBeanName));
                    extendedBeanDefinitionRegistry.registerBeanDefinition(graphiteMockBeanName, graphiteMockBeanDefinition, meta.getBeanQualifiers());

                    RootBeanDefinition graphiteMeterRegistryBeanDefinition = new RootBeanDefinition(GraphiteMeterRegistry.class);
                    graphiteMeterRegistryBeanDefinition.setFactoryBeanName(graphiteMeterRegistryFactoryBeanName);
                    graphiteMeterRegistryBeanDefinition.setFactoryMethodName("createGraphiteMeterRegistry");
                    graphiteMeterRegistryBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(metricRegistryBeanName));
                    graphiteMeterRegistryBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, new RuntimeBeanReference(graphiteConfigBeanName));
                    graphiteMeterRegistryBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(2, new RuntimeBeanReference(graphiteReporterBeanName));
                    extendedBeanDefinitionRegistry.registerBeanDefinition(meta.name(), graphiteMeterRegistryBeanDefinition, meta.getBeanQualifiers());
                });
            }
        }
    }

    class GraphiteReporterFactory {
        GraphiteReporter createGraphiteReporter(MetricRegistry metricRegistry, GraphiteConfig config, GraphiteSenderMock graphiteSenderMock) {
            return GraphiteReporter
                .forRegistry(metricRegistry)
                .convertRatesTo(config.rateUnits())
                .convertDurationsTo(config.durationUnits())
                .build(graphiteSenderMock);
        }
    }

    class GraphiteMeterRegistryFactory {
        GraphiteMeterRegistry createGraphiteMeterRegistry(MetricRegistry metricRegistry, GraphiteConfig config, GraphiteReporter graphiteReporter) {
            return new GraphiteMeterRegistry(config, Clock.SYSTEM, new GraphiteHierarchicalNameMapper(config.tagsAsPrefix()), metricRegistry, graphiteReporter);
        }
    }
}
