package com.github.fridujo.automocker.api.metrics;

import java.util.List;

public class AbstractMetricAsserter {

    protected final String metricName;
    protected final List<Point> points;

    protected AbstractMetricAsserter(String metricName, List<Point> points) {
        this.metricName = metricName;
        this.points = points;
    }

    protected Point getLastPoint() {
        if (points.isEmpty()) {
            throw new AssertionError("No values recorded for metric " + metricName);
        }
        return points.get(points.size() - 1);
    }

    protected Point getPenultimatePoint() {
        final Point penultimatePoint;
        if (points.size() > 1) {
            penultimatePoint = points.get(points.size() - 2);
        } else {
            penultimatePoint = Point.NEUTRAl_ELEMENT;
        }
        return penultimatePoint;
    }
}
