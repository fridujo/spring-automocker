package com.github.fridujo.automocker.api.metrics;

public class Point {
    static final Point NEUTRAl_ELEMENT = new Point(0, 0);
    final long timestamp;
    final double value;

    Point(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
