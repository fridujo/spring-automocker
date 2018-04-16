package com.github.fridujo.automocker.utils;

public final class Classes {

    private Classes() {
    }

    public static <T> T instanciate(Class<? extends T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot instanciate class " + clazz.getName() + ": " + e.getMessage(), e);
        }
    }
}
