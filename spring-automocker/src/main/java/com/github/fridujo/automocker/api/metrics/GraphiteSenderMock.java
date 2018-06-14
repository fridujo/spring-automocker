package com.github.fridujo.automocker.api.metrics;

import com.codahale.metrics.graphite.GraphiteSender;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GraphiteSenderMock implements GraphiteSender {

    private final Map<String, List<Point>> metrics = new LinkedHashMap<>();

    @Override
    public void connect() {
    }

    @Override
    public void send(String name, String value, long timestamp) {
        if (!metrics.containsKey(name)) {
            metrics.put(name, new ArrayList<>());
        }
        metrics.get(name).add(new Point(timestamp, Double.valueOf(value)));
    }

    @Override
    public void flush() {
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public int getFailures() {
        return 0;
    }

    @Override
    public void close() {
    }

    public Map<String, List<Point>> getMetrics() {
        return metrics;
    }
}
