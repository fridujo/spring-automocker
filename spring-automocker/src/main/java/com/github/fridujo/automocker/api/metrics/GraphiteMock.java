package com.github.fridujo.automocker.api.metrics;

import com.codahale.metrics.graphite.GraphiteReporter;

public class GraphiteMock {

    private final GraphiteSenderMock graphiteSenderMock;
    private final GraphiteReporter graphiteReporter;

    public GraphiteMock(GraphiteSenderMock graphiteSenderMock, GraphiteReporter graphiteReporter) {
        this.graphiteSenderMock = graphiteSenderMock;
        this.graphiteReporter = graphiteReporter;
    }

    public MetricAsserter assertThatMetric(String metricName) {
        if (!graphiteSenderMock.getMetrics().containsKey(metricName)) {
            throw new IllegalArgumentException("No metric named [" + metricName + "] have been sent to Graphite");
        }
        return new MetricAsserter(metricName, graphiteSenderMock.getMetrics().get(metricName));
    }

    public GraphiteMock afterReporting() {
        graphiteReporter.report();
        return this;
    }
}
