package com.github.fridujo.automocker.api.metrics;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricAsserter extends AbstractMetricAsserter {

    public MetricAsserter(String metricName, List<Point> points) {
        super(metricName, points);
    }

    public void hasLastValue(double expectedValue) {
        Point lastPoint = getLastPoint();

        assertThat(lastPoint.value)
            .as("Last recorded value of metric [" + metricName + "]")
            .isEqualTo(expectedValue);
    }

    public DerivativeMetricAsserter derivative() {
        return new DerivativeMetricAsserter(metricName, points);
    }
}
