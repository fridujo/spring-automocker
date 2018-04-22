package com.github.fridujo.automocker.utils;

import java.util.Properties;

public final class PropertiesBuilder {
    private PropertiesBuilder() {
    }

    public static Properties of(String key, String value) {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        return properties;
    }
}
