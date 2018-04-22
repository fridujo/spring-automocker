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

    public static boolean isPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return false;
        }
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot load class " + className);
        }
    }
}
