package com.github.fridujo.automocker.api.metrics;

public class GraphiteMock extends AbstractGraphiteMock {

    public MetricAsserter assertThatMetric(String metricName) {
        if (!metrics.containsKey(metricName)) {
            throw new IllegalArgumentException("No metric named [" + metricName + "] have been sent to Graphite");
        }
        return new MetricAsserter(metricName, metrics.get(metricName));
    }
}
