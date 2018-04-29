package com.github.fridujo.automocker.api.metrics;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DerivativeMetricAsserter extends AbstractMetricAsserter {

    public DerivativeMetricAsserter(String metricName, List<Point> points) {
        super(metricName, points);
    }

    public void hasLastValue(int expectedValue) {
        Point lastPoint = getLastPoint();
        final Point penultimatePoint = getPenultimatePoint();

        assertThat(lastPoint.value - penultimatePoint.value)
            .as("Last recorded derivative of metric [" + metricName + "]")
            .isEqualTo(expectedValue);
    }
}
